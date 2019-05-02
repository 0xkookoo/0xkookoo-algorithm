/**
 * @author bingo
 * @since 2018/11/20
 */

public class Solution {

    class ListNode {
        int val;
        ListNode next;
    }

    /**
     * 删除链表的节点
     * 
     * @param head       链表头节点
     * @param tobeDelete 要删除的节点
     */
    public ListNode deleteNode(ListNode head, ListNode tobeDelete) {
        if (head == null || tobeDelete == null) {
            return head;
        }

        // 删除的不是尾节点
        if (tobeDelete.next != null) {
            tobeDelete.val = tobeDelete.next.val;
            tobeDelete.next = tobeDelete.next.next;
        }
        // 链表中仅有一个节点
        else if (head == tobeDelete) {
            head = null;
        }
        // 删除的是尾节点
        else {
            // 当head遍历到tobeDelete时，指向的是同一个引用。可以直接将tobeDelete = null即可
            tobeDelete = null;
        }

        return head;
    }
}