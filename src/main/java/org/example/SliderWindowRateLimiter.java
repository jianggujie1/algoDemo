package org.example;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SliderWindowRateLimiter implements Runnable {

    // 每单位时间最大请求
    private final long maxRequestPerTime;

    // 将单位时间内划分成 block 个块
    private final int block;

    // 每块计数
    private final AtomicLong[] countPerBlock;

    // 滑动窗口起始位置
    private volatile int index;

    // 单位时间内的总计数
    private AtomicLong allCount;

    public SliderWindowRateLimiter(long maxRequestPerTime, int block) {
        this.maxRequestPerTime = maxRequestPerTime;
        this.block = block;
        countPerBlock = new AtomicLong[block];
        for (int i = 0; i < countPerBlock.length; i++) {
            countPerBlock[i] = new AtomicLong();
        }
        allCount = new AtomicLong();
    }

    public boolean isOverLimit() {
        return currentQPS() > maxRequestPerTime;
    }

    public long currentQPS() {
        return allCount.get();
    }

    public boolean visit() {
        countPerBlock[index].incrementAndGet();
        allCount.incrementAndGet();

        if (isOverLimit()) {
            System.out.println(Thread.currentThread().getName() + "被限流" + "，currentQPS：" + currentQPS() + "，index：" +
                    index + ", perCount：" + countPerBlock[index].get());
            return false;
        } else {
            System.out.println(Thread.currentThread().getName() + "执行业务逻辑" + "，currentQPS：" + currentQPS() + "，index：" +
                    index + ", perCount：" + countPerBlock[index].get());
            return true;
        }
    }

    @Override
    public void run() {
        System.err.println("----------------递进ing-----------------");
        // 1.往前递进一个小格
        index = (index + 1) % block;
        // 2.获取当前索引的值
        long perCount = countPerBlock[index].getAndSet(0);
        // 3.总数减去新格子的值
        allCount.addAndGet(-perCount);
    }

    public static void main(String[] args) {
        SliderWindowRateLimiter sliderWindowRateLimiter = new SliderWindowRateLimiter(100, 100);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(sliderWindowRateLimiter, 30, 25, TimeUnit.MILLISECONDS);
        new Thread(() -> {
            while (true) {
                try {
                    if (sliderWindowRateLimiter.visit()) {
                        Thread.sleep(20);
                    } else {
                        Thread.sleep(60);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    if (sliderWindowRateLimiter.visit()) {
                        Thread.sleep(35);
                    } else {
                        Thread.sleep(75);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
