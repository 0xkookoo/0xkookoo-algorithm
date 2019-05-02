## 数据流中的中位数

### 题目描述
如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。我们使用`Insert()`方法读取数据流，使用`GetMedian()`方法获取当前读取数据的中位数。

### 解法
利用大根堆存放较小的一半元素，小根堆存放较大的一半元素。维持大小堆的元素个数差不超过 1。


```java
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author bingo
 * @since 2018/12/7
 */

public class Solution {

    private PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

    /**
     * 插入一个数
     *
     * @param num 数
     */
    public void Insert(Integer num) {

        if (maxHeap.isEmpty() || num < maxHeap.peek()) {
            maxHeap.offer(num);
            if (maxHeap.size() - minHeap.size() > 1) {
                minHeap.offer(maxHeap.poll());
            }

        } else {
            minHeap.offer(num);
            if (minHeap.size() - maxHeap.size() > 1) {
                maxHeap.offer(minHeap.poll());
            }
        }
    }

    /**
     * 获取中位数
     *
     * @return 中位数
     */
    public Double GetMedian() {
        int size1 = maxHeap.size();
        int size2 = minHeap.size();
        if (size1 > size2) {
            return (double) maxHeap.peek();
        }
        if (size1 < size2) {
            return (double) minHeap.peek();
        }

        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
```

### 测试用例
1. 功能测试（从数据流中读出奇数/偶数个数字）；
2. 边界值测试（从数据流中读出 0/1/2 个数字）。