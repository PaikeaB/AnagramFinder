HASH:
0.286, 0.375, 0.396, 0.284, 0.287 // AVG: 0.3256
AVL: 
0.675, 0.709, 0.624, 0.708, 0.745 // AVG: 0.6922
BST:
1.841, 1.761, 1.704, 1.594, 1.777 // AVG: 1.7354

I expect the hash table to be the fastest, averaging O(1) time for insertions and looking up data.
Hash tables would not need to traverse tree structures. AVL tree should be next, due to its balancing feature. AVL trees have an average O(lg n) time complexity for insertions and lookups.
This is more efficient than an unbalanced BST which has worst-case O(n) complexity for insertion and lookup. This was corroborated by my data above. This makes sense as the BST, in worst case, could become a linked list, while an AVL would never let this happen due to its balancing factor, which means it will have O(lg n) time complexity as stated above. Hash maps obviously win in the time complexity department, averaging O(1) time.