package org.example;

/**
 * 判断回文链表
 * 234. 回文链表
 */
public class PalindromeSolution {
//    普通模式，时空间复杂度都为O(n)
    /*private static ListNode left;

    private static boolean traverse(ListNode right) {
        if (right == null) {
            return true;
        }
        boolean traverse = traverse(right.next);
        System.err.println(right.val);
        boolean b = traverse && (left.val == right.val);
        left = left.next;
        return b;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(3);
        head.next.next.next.next = new ListNode(2);
        head.next.next.next.next.next = new ListNode(1);
        left = head;
        System.out.println("是否回文：" + traverse(head));
    }*/

//    优化后的回文判断，空间复杂度O(1) 时间复杂度O(n)
    private static ListNode reverse(ListNode head) {
        ListNode pre, cur;
        pre = null;
        cur = head;
        // 迭代主要过程
        while (cur != null) {
            ListNode nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }

    private static boolean isPalindrome(ListNode head) {
        //设置快慢指针，找到中点
        ListNode slow, fast;
        slow = fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        //如果fast节点不为空，则是奇数个，则反方向的链表是中点后一个
        if (fast != null) {
            slow = slow.next;
        }

        ListNode left, right;
        left = head;
        right = reverse(slow);

        //开始回文判断
        while (right != null) {
            if (left.val != right.val) {
                return false;
            }
            left = left.next;
            right = right.next;
        }
        return true;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(3);
        head.next.next.next.next = new ListNode(2);
        head.next.next.next.next.next = new ListNode(1);
        boolean palindrome = isPalindrome(head);
        System.out.println(palindrome);
    }
}
