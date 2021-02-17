package com.wissen.hackerrank.sixth.kunduandtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
1. How to solve the problem?
This problem is solely based on following observations.
If there is a red edge between vertex 'a' and 'b' then if we delete the red edge then vertex a and b will lie on
different component of the tree. So, if we delete all of the red edges in the tree. All the three vertices i.e. a, b
and c will have to lie of different disconnected components. So, when you are scanning the tree and got that color of
some edge is red, don't scan it and make forest i.e. disconnected tree.

2. How to create tree data structure in Java?

3. How to store multiple tree in Java in such a way that we add the edge in the associated tree of the forest?

4. Counting triplets with red edges in each pair
https://math.stackexchange.com/questions/838792/counting-triplets-with-red-edges-in-each-pair?newreg=60eee35f0b3844de852bda39f6dfec88
##############STEPS##############Todo: Complete this.
##############QUESTION##############
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/kundu-and-tree
Kundu is true tree lover. Tree is a connected graph having N vertices and N-1 edges. Today when he got a tree,
he colored each edge with one of either red(r) or black(b) color. He is interested in knowing how many
triplets(a,b,c) of vertices are there , such that, there is atleast one edge having red color on all the three
paths i.e. from vertex a to b, vertex b to c and vertex c to a . Note that (a,b,c), (b,a,c) and all such permutations
will be considered as the same triplet.

If the answer is greater than 109 + 7, print the answer modulo (%) 109 + 7.

Input Format
The first line contains an integer N, i.e., the number of vertices in tree.
The next N-1 lines represent edges: 2 space separated integers denoting an edge followed by a color of the edge. A
color of an edge is denoted by a small letter of English alphabet, and it can be either red(r) or black(b).

Output Format
Print a single number i.e. the number of triplets.

Constraints
1 ≤ N ≤ 105
A node is numbered between 1 to N.

Sample Input
5
1 2 b
2 3 r
3 4 r
4 5 b

Sample Output
4

Explanation
Given tree is something like this.

1-b-2-r-3-r-4-b-5

(2,3,4) is one such triplet because on all paths i.e 2 to 3, 3 to 4 and 2 to 4 there is atleast one edge having red
color.(2,3,5), (1,3,4) and (1,3,5) are other such triplets.

Note that (1,2,3) is NOT a triplet, because the path from 1 to 2 does not have an edge with red color.
 */
public class Solution {

    class QueryProcessor {

        /**
         * STEP 1: Define a Data class
         * This class provides implementation for storage of elements in disjoint set data structure.
         */
        class Data {
            private final long data;

            public Data(long data) {
                this.data = data;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Data data1 = (Data) o;
                return data == data1.data;
            }

            @Override
            public int hashCode() {
                return Objects.hash(data);
            }
        }

        /**
         * STEP 2: Define a DisjointSet generic class
         * This class provides implementation of disjoint set data structure.
         */
        class DisjointSet<T> {

            public Map<T, Node> nodeByData = new HashMap<>();

            /**
             * STEP 3: Define a Node class
             * This class provides implementation for storage of node in disjoint set data structure.
             */
            private class Node {
                private final T data;
                private int rank;
                private Node parent;

                public Node(T data) {
                    this.data = data;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    Node node = (Node) o;
                    return rank == node.rank && Objects.equals(data, node.data) && Objects.equals(parent, node.parent);
                }

                @Override
                public int hashCode() {
                    return Objects.hash(data, rank, parent);
                }
            }

            /**
             * STEP 4: Define a makeSet() method in DisjointSet class
             * This method takes one record and adds to the disjoint set.
             *
             * @param data the record which needs to be inserted in the disjoint set.
             */
            public void makeSet(T data) {
                Node node = new Node(data);
                node.parent = node;
                node.rank = 0;
                nodeByData.put(data, node);
            }

            /**
             * STEP 5: Define a union() method in DisjointSet class
             * This method takes two record and finds out the group of both the record if both records are from
             * different then both are the groups are merged into one group.
             *
             * @param firstData  the first record for which group needs to be found and merged.
             * @param secondData the second record for which group needs to be found and merged.
             */
            public void union(T firstData, T secondData) {
                Node firstNode = nodeByData.get(firstData);
                Node secondNode = nodeByData.get(secondData);

                Node firstNodeParent = findSet(firstNode);
                Node secondNodeParent = findSet(secondNode);

                if (firstNodeParent.data == secondNodeParent.data) {
                    return;
                }

                if (firstNodeParent.rank >= secondNodeParent.rank) {
                    firstNodeParent.rank = (firstNodeParent.rank == secondNodeParent.rank) ?
                            firstNodeParent.rank + 1 : firstNodeParent.rank;
                    secondNodeParent.parent = firstNodeParent;
                } else {
                    firstNodeParent.parent = secondNodeParent;
                }
            }

            /**
             * STEP 5: Define a findSet() method in DisjointSet class
             * This method takes a node as input and finds the root node for the same input node.
             *
             * @param firstNode the node for which root node needs to be found.
             */
            public Node findSet(Node firstNode) {
                Node parent = firstNode.parent;
                if (parent == firstNode) {
                    return parent;
                }
                firstNode.parent = findSet(firstNode.parent);
                return firstNode.parent;
            }
        }

        private final DisjointSet<Data> disjointSet = new DisjointSet<>();

        QueryProcessor(long nodeCount) {

            for (long i = 1; i <= nodeCount; i++) {
                disjointSet.makeSet(new Data(i));
            }
        }

        /**
         * STEP 6: Define a processQuery() method in QueryProcessor class
         * This method takes three query parameter which are the source node identifier and destination node
         * identifier which need to be linked with an edge and the color of the edge which links the first and second
         * node.
         *
         * @param sourceNodeIdentifier      the source node of the edge.
         * @param destinationNodeIdentifier the destination node of the edge.
         * @param color                     the color of edge between the first and second node.
         */
        private void processQuery(int sourceNodeIdentifier, int destinationNodeIdentifier, char color) {
            switch (color) {
                case 'r':

                    break;
                case 'b':
                    disjointSet.union(new Data(sourceNodeIdentifier), new Data(destinationNodeIdentifier));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Color");
            }
        }

        /**
         * STEP 7: Define a processQuery() method in QueryProcessor class
         * This method takes query as input which contains the source node identifier and destination node identifier
         * which need to be linked with an edge and the color of the edge which links the first and second node.
         *
         * @param query the source node of the edge.
         */
        public void processQuery(String query) {
            if (query == null || query.length() <= 0) {
                return;
            }
            String[] param = query.split(" ");
            if (param.length != 3 || !Character.isDigit(param[0].charAt(0)) ||
                    !Character.isDigit(param[1].charAt(0)) || !Character.isAlphabetic(param[2].charAt(0))) {
                return;
            }
            processQuery(Integer.parseInt(param[0]), Integer.parseInt(param[1]), param[2].charAt(0));
        }

        /**
         * STEP 8: Define a tripletsCount() method in QueryProcessor class
         * This method calculates and returns the count of the triplets which contains atleast one red edge.
         *
         * @return The count of the triplets which contains atleast one red edge.
         */
        public long tripletsCount() {
            return calculateTriplets(disjointSet.nodeByData.size(),
                    calculateSizeOfIndividualGroup(disjointSet.nodeByData));
        }

        /**
         * STEP 9: Define a calculateSizeOfIndividualGroup() method in QueryProcessor class
         * This method takes a map containing node to group mapping and returns the count of nodes associated with all
         * the groups part of disjoint set.
         *
         * @param nodeByData map containing node to group mapping.
         * @return The count of nodes associated with all the groups part of disjoint set.
         */
        private List<Long> calculateSizeOfIndividualGroup(Map<Data, DisjointSet<Data>.Node> nodeByData) {
            Map<Data, Long> groupSizeByData = new HashMap<>();
            for (Map.Entry<Data, DisjointSet<Data>.Node> person : nodeByData.entrySet()) {
                DisjointSet<Data>.Node parent = disjointSet.findSet(person.getValue());
                Long output = groupSizeByData.putIfAbsent(parent.data, 1L);
                if (output != null) {
                    groupSizeByData.put(parent.data, output + 1);
                }
            }
            return new ArrayList<>(groupSizeByData.values());
        }

        /**
         * STEP 10: Define a calculateTriplets() method in QueryProcessor class
         * This method takes total number of nodes in the disjoint set and node count of all the groups part of the
         * disjoint set and returns the count of the triplets which contains atleast one red edge.
         *
         * @param verticesCount      total number of nodes in the disjoint set.
         * @param totalGroupSizeList node count of all the groups part of the disjoint set.
         * @return The count of the triplets which contains atleast one red edge.
         */
        private long calculateTriplets(long verticesCount, List<Long> totalGroupSizeList) {
            if (verticesCount <= 0 || totalGroupSizeList == null || totalGroupSizeList.size() == 0) {
                return 0;
            }
            long mod = 1000000007;
            long sum = (verticesCount * (verticesCount - 1) * (verticesCount - 2)) / 6;

            long diff = 0;

            long binom2 = 0;
            long binom3 = 0;
            for (Long individualGroupSize : totalGroupSizeList) {
                binom2 = 0;
                binom3 = 0;
                if (individualGroupSize > 2) {
                    binom3 += (individualGroupSize * (individualGroupSize - 1) * (individualGroupSize - 2)) / 6;
                }
                binom2 = (individualGroupSize * (individualGroupSize - 1)) / 2;
                diff += (binom3 + binom2 * (verticesCount - individualGroupSize));
            }
            return (sum - diff) % mod;
        }
    }

    public void init(Scanner scanner) {
        int verticesCount = scanner.nextInt();
        scanner.nextLine();
        QueryProcessor queryProcessor = new QueryProcessor(verticesCount);
        for (long i = 1; i < verticesCount; i++) {
            String query = scanner.nextLine();
            queryProcessor.processQuery(query);
        }
        System.out.println(queryProcessor.tripletsCount());
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Solution().init(SCANNER);
    }
}
