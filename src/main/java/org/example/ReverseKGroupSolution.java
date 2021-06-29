package org.example;

/**
 * 如何k个一组反转链表
 * 25. K 个一组翻转链表
 */
public class ReverseKGroupSolution {
    private static ListNode reverse(ListNode a, ListNode b) {
        ListNode pre, cur, nxt;
        pre = null;
        cur = a;
        nxt = b;
        // 迭代主要过程
        while (cur != b) {
            nxt = cur.next;

            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }

    private static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        ListNode a, b;
        a = b = head;
        //判断是否够k个元素，不够则直接返回
        for (int i = 0; i < k; i++) {
            if (b == null) {
                return head;
            }
            b = b.next;
        }
        // 反转[a,b] 即从a开始到第k个之间的反转 迭代
        ListNode newHead = reverse(a, b);

        // 递归，从b开始继续迭代反转k个元素，并连接起来
        a.next = reverseKGroup(b, k);

        //此时newHead为新的头节点
        return newHead;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        ListNode listNode = reverseKGroup(head, 2);
        while (listNode != null) {
            System.out.print(listNode.val + " ");
            listNode = listNode.next;
        }
    }
}
