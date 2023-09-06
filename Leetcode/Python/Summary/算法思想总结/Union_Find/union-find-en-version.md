# Union Find (Reference to leetcode323 problem)

## Application Scenarios:

Dynamic connectivity

- Network connection judgment:  
    If each pair of integers is used to indicate that these two nodes need to be connected, then establishing a dynamic connectivity graph for all pairs can minimize the wiring needs as much as possible,  
    because the two nodes that are already connected will be directly ignored. For example, [1,2] represents that nodes 1 and 2 are connected. If [2,1] appears again, we don't need to connect them again, because they have been connected once.
    
- Variable name identity (similar to the concept of pointers):  
    Multiple references can be declared in the program to point to the same object. At this time, a dynamic connectivity graph can be established for the declared references and the actual objects in the program to determine which references actually point to the same object.
    

## Problem Modeling:

When modeling the problem, we should try to clarify what problem needs to be solved as much as possible. Because the data structure and algorithm selected in the model will definitely vary depending on the problem, for the dynamic connectivity scenario, the problems we need to solve may be:

1. Given two nodes, determine if they are connected. If connected, there is no need to give the specific path.
    
2. Given two nodes, determine if they are connected. If connected, need to give the specific path.
    

For the above two problems, although there is only a difference in whether a specific path can be given, this difference leads to the difference in algorithm selection.

## Modeling Ideas:

The simplest and most intuitive assumption is that all connected nodes can be considered as a group, so non-connected nodes must belong to different groups. With the input of pairs, we need to first determine whether the two input nodes are connected. How to determine? According to the above assumption, we can determine through the group that they belong to, and then see if these two groups are the same. If the same, then the two nodes are connected; otherwise not connected. For simplicity, we use integers to represent the N nodes, that is, using integers from 0 to N-1 to represent. While before processing the input pairs, each node must be isolated, i.e. they belong to different groups. An array can be used to represent this layer of relationship. The array index is the integer representation of the node, and the corresponding value is the group number of the node. This array can be initialized as:

Copy

```
uf = [i for i in range(n)]  
```

After initialization is complete, there are several possible operations for this dynamic connectivity graph:

1. Query the group that a node belongs to

The corresponding position of the array is the group number

2. Determine if two nodes belong to the same group

Get the group numbers of the two nodes respectively and determine if the group numbers are equal

3. Connect two nodes to belong to the same group

Get the group numbers of the two nodes respectively. If the group numbers are the same, the operation ends; otherwise, change the group number of one node to the group number of the other node.

4. Get the number of groups

Initialized as the number of nodes. Then decrement 1 each time two groups are successfully merged.

## API:

We can set the corresponding API:

python

Copy

```
def uf(n):   # Initialize uf array and number of groups  
def union(x, y): # Connect two nodes
def find(x): # Determine the group that a node belongs to
def connected(x, y): # Determine if two nodes are connected  
def count(): # Return the number of all groups
```

Note that integers are used here to represent nodes. If other data types need to be used to represent nodes, such as strings, hash tables can be used to map Strings to the required Integer types here.

Analyzing the above API, the methods connected and union both depend on find. Connected calls find method twice for the two parameters, and union needs to determine whether connected before the actual union, which is also two find calls. Therefore, we need to design the find method to be as efficient as possible. This is why Quick-Find is introduced.

### Quick-Find Implementation

python

Copy

```
class Solution(object):
  uf = []    # access to component id (site indexed)
  count = 0  # number of components

  def uf(self, n):  # Initialize uf array and number of groups  
    self.count = n
    self.uf = [i for i in range(n)]     
        
  def find(self, x):  # Determine the group that a node belongs to
    return uf[x]

  def union(self, x, y):  # Connect two nodes
    x_root = find(x)
    y_root = find(y)
    if x_root == y_root:
      return
    for i in range(len(self.uf)):
      if uf[i] == x_root:
        uf[i] = y_root
    count -= 1
  
  def connected(self, x, y):  # Determine if two nodes are connected
    return find(x) == find(y)
  
  def count(self):  # Return the number of all groups
    return count               
```

The find method in the above code is very efficient, as it only requires one array access operation to find the group number of the node.

However, problems arise when it comes to adding new paths, as it involves modifying group numbers, and the locations that need to be modified cannot be determined, so the entire array needs to be traversed to find and modify the nodes one by one, which brings the time complexity of each path addition to a linear relationship. If the number of new paths to add is M and the number of nodes is N, the final time complexity becomes O(MN), which is a quadratic complexity and is not suitable for large-scale data.

In order to solve this problem, we need to improve the efficiency of the union method so that it does not need to traverse the entire array. What kind of data structure can organize nodes in a better way? Thinking of data structures learned before, trees are obviously the best choice for faster find and modify operations.

### Quick-Union Algorithm:

If we do not change the underlying data structure and still use arrays for representation, we can organize nodes into trees with parent pointers.

For example, uf[p] stores the parent node number of p. If p is the root of the tree, uf[p] is p. Therefore, through a series of lookups, a node can always find the root of the tree it belongs to, which is also the representative of the group. Then the root number can be used to represent the group number.

Therefore, when handling a pair, we first find the root numbers of the two nodes in the pair, and if they are different, connect one root to be the child of the other root to merge the two trees.

Intuitively, the process is as shown in the following figure. However, this introduces a new problem.

python

Copy

```
class Solution:
  uf = []

  def __init__(self, n):
    self.uf = [i for i in range(n)]

  def find(self, x):
    while x != self.uf[x]:
      x = self.uf[x]
    return x

  def union(self, x, y):
    xRoot = self.find(x)  
    yRoot = self.find(y)
    if xRoot == yRoot:
      return
    self.uf[yRoot] = xRoot
```

To overcome this problem, we can consider the size of the trees and always merge the smaller tree to the larger tree to keep the structure balanced.

python

Copy

```
def union(self, x, y):
  xRoot = find(x)
  yRoot = find(y)
  
  if size[xRoot] > size[yRoot]:
    uf[yRoot] = xRoot 
    size[xRoot] += size[yRoot]
  else:  
    uf[xRoot] = yRoot
    size[yRoot] += size[xRoot]
```

This improves the efficiency of both union and find operations, bringing the overall time complexity down to near linear.

The above reasoning also gives us some inspiration: For the ideal structure of Quick-Union trees, all child nodes should connect directly to the root. This way can ensure the highest efficiency of find operations.

So how to construct such an ideal structure? In the process of find, we save all the intermediate nodes encountered to an array, and then link their parent nodes to the root after while loop. However, this method has problems too.

A smarter way is to compress the path during find by making the parent of the current node point directly to its grandparent.

python

Copy

```
def find(self, x):
  while x != uf[x]:
    uf[x] = uf[uf[x]]
    x = uf[x]
  return x
```

This achieves path compression lazily during finds instead of through an extra data structure. Both union and find can be performed in O(α(n)) time on average.

### Time complexity analysis:

- find() operation has worst-case time complexity of O(N)
- union() operation has worst-case time complexity of O(1) 

The performance of Quick union will degrade as we keep calling union() to build the connectivity structure. Because the representative tree of each component keeps getting taller, find() will take longer.

By this point, the introduction of Union-Find algorithms for dynamic connectivity problems has been basically covered, from the easier Quick-Find to the relatively complex but more efficient Quick-Union, and then to the several improvements made to Quick-Union, which allows the efficiency of our algorithms to increase steadily.

The time complexity of these algorithms is shown below:

![Image of time complexity table](https://poe.com/chat/2jpq42199g52mjyx5hy)

For large-scale data processing, quadratic algorithms are unsuitable, such as the simple and intuitive Quick-Find algorithm. By discovering more features of the problem, finding a suitable data structure, and then making targeted improvements, the Quick-Union algorithm and its various improved algorithms were obtained, finally making the algorithm complexity lowered to near-linear complexity.

If the required function is not just to detect whether two nodes are connected, but also to obtain the specific path when connected, then other algorithms need to be used, such as DFS or BFS.

For applications of Union-Find, refer to another article [Union-Find application examples ↗](https://blog.csdn.net/dm_vincent/article/details/7769159)

## References

1. [Introduction to Union-Find algorithm ↗](https://blog.csdn.net/dm_vincent/article/details/7655764)
