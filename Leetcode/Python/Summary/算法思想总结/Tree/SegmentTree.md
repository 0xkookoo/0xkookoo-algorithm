Let us consider the following problem to understand Segment Trees.

We have an array arr[0 . . . n-1]. We should be able to
1. Find the sum of elements from index l to r where 0 <= l <= r <= n-1
2. Change value of a specified element of the array to a new value x. We need to do arr[i] = x where 0 <= i <= n-1.

A simple solution is to run a loop from l to r and calculate sum of elements in given range. To update a value, simply do arr[i] = x. 
The first operation takes O(n) time and second operation takes O(1) time.

Another solution is to create another array and store sum from start to i at the ith index in this array. 
Sum of a given range can now be calculated in O(1) time, but update operation takes O(n) time now. 
This works well if the number of query operations are large and very few updates.

What if the number of query and updates are equal? 
Can we perform both the operations in O(log n) time once given the array? 
We can use a Segment Tree to do both operations in O(Logn) time.

Representation of Segment trees
1. Leaf Nodes are the elements of the input array.
2. Each internal node represents some merging of the leaf nodes. 
The merging may be different for different problems. For this problem, merging is sum of leaves under a node.

详细见[Segment Tree | Set 1 (Sum of given range)](https://www.geeksforgeeks.org/segment-tree-set-1-sum-of-given-range/)

还可以看看[这里的solution3介绍](https://leetcode.com/problems/range-sum-query-mutable/solution/)

下面是我的python实现，可以作为模版

```python
class SegmentTree(object):
    def __init__(self, nums): # O(N)
        """
        :type nums: List[int]
        """
        self.n = len(nums)
        self.tree = [0] * (2 * self.n)

        def buildTree(nums):
            self.tree[self.n:] = nums[:]
            for i in range(self.n - 1, 0, -1):
                self.tree[i] = self.tree[2 * i] + self.tree[2 * i + 1]

        buildTree(nums)

    def update(self, i, val): # O(lgN)
        """
        :type i: int
        :type val: int
        :rtype: void
        """
        i += self.n
        self.tree[i] = val
        while i > 0:
            self.tree[i // 2] = self.tree[i // 2 * 2] + self.tree[i // 2 * 2 + 1]
            i //= 2

    def sumRange(self, i, j): # O(lgN)
        """
        :type i: int
        :type j: int
        :rtype: int
        """
        i, j = i + self.n, j + self.n
        sums = 0
        while i <= j:
            if i % 2 == 1:  # 左边多出一个不能成对的
                sums += self.tree[i]
                i += 1
            if j % 2 == 0:  # 右边多出一个不能成对的
                sums += self.tree[j]
                j -= 1
            i //= 2
            j //= 2
        return sums


nums = [1, 3, 5, 7, 9, 11]
s = SegmentTree(nums)
print("Sum of values in given range = ", s.sumRange(1, 3))
s.update(1, 9)
print("Updated sum of values in given range = ", s.sumRange(1, 3))



Sum of values in given range =  15
Updated sum of values in given range =  21

```







# 区间最大值查询，更新

```python
# Python3 code for range maximum query and updates
from math import ceil, log


def getMid(s, e):
    return s + (e - s) // 2


def MaxUtil(st, ss, se, l, r, node):
    if (l <= ss and r >= se):
        return st[node]
    if (se < l or ss > r):
        return -float('inf')
    mid = getMid(ss, se)
    return max(MaxUtil(st, ss, mid, l, r,
                       2 * node + 1),
               MaxUtil(st, mid + 1, se, l,
                       r, 2 * node + 2))


def updateValue(arr, st, ss, se, index, value, node):
    if (index < ss or index > se):
        # print("Invalid Input")
        return

    if (ss == se):
        arr[index] = value
        st[node] = value
    else:
        mid = getMid(ss, se)
        if (index >= ss and index <= mid):
            updateValue(arr, st, ss, mid, index,
                        value, 2 * node + 1)
        else:
            updateValue(arr, st, mid + 1, se,
                        index, value, 2 * node + 2)

        st[node] = max(st[2 * node + 1],
                       st[2 * node + 2])
    return


def getMax(st, n, l, r):
    if (l < 0 or r > n - 1 or l > r):
        # print("Invalid Input")
        return -float('inf')

    return MaxUtil(st, 0, n - 1, l, r, 0)


def constructSTUtil(arr, ss, se, st, si):
    if (ss == se):
        st[si] = arr[ss]
        return arr[ss]
    mid = getMid(ss, se)

    st[si] = max(constructSTUtil(arr, ss, mid, st,
                                 si * 2 + 1),
                 constructSTUtil(arr, mid + 1, se,
                                 st, si * 2 + 2))

    return st[si]


def constructST(arr):
    x = ceil(log(len(arr), 2))
    max_size = 2 * pow(2, x) - 1
    st = [-float('inf')] * max_size
    constructSTUtil(arr, 0, len(arr) - 1, st, 0)
    return st


# Driver code
if __name__ == '__main__':
    arr = [-10,-34,-13]
    n = len(arr)

    # Build segment tree from given array 
    st = constructST(arr)
    print(st)

    # Prmax of values in array 
    # from index 1 to 3
    for i in range(len(arr)):
        for j in range(i+1,len(arr)):
            print("Max of values in given range = " + str(i) + ' ' + str(j), getMax(st, n, i, j))
    print("Max of values in given range = ", getMax(st, n, 1, 3))

    # Update: set arr[1] = 8 and update 
    # corresponding segment tree nodes. 
    updateValue(arr, st, 0, n - 1, 1, 8, 0)

    # Find max after the value is updated 
    print("Updated max of values in given range = ", getMax(st, n, 1, 3))

    # This code is contributed by mohit kumar 29
```


具体应用看problem: `Max Value of Equation`





























               
