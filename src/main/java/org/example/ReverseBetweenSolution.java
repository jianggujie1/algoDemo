package org.example;

/**
 * 递归反转链表
 * 92. 反转链表 II
 */
public class ReverseBetweenSolution {

    private static ListNode successor = null;

    private static ListNode reverseBetween(ListNode head, int left, int right) {
        //base case
        if (left == 1) {
            return reverseN(head, right);
        }
        // 前进到反转的起点出发 base case
        head.next = reverseBetween(head.next, left - 1, right - 1);
        return head;
    }

    private static ListNode reverseN(ListNode head, int n) {
        //base case
        if (n == 1) {
            successor = head.next;
            return head;
        }
        // 以head.next 为起点，需要反转前n-1个节点，那当前head.next节点为最终要返回的节点，即头节点，和base case呼应
        ListNode last = reverseN(head.next, n - 1);
        head.next.next = head;
        head.next = successor;

        return last;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        ListNode listNode = reverseBetween(head, 4, 5);
        while (listNode != null) {
            System.out.print(listNode.val + " ");
            listNode = listNode.next;
        }
    }
}
