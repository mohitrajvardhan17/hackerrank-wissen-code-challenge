/*
##############NOTES##############Todo: Complete this.
Facts while solving the problem:
1. All edges are bi-directional hence the weight between node X and node Y is same as node Y and node x.
2. Always cost between two node is the maximum weight of any edge which is between the staring and ending node.
##############STEPS##############Todo: Complete this.
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/maximum-cost-queries
Victoria has a tree, T, consisting of N nodes numbered from 1 to N. Each edge from node Ui to Vi in tree T has an
integer weight, Wi.

Let's define the cost, C, of a path from some node X to some other node Y as the maximum weight (W) for any edge in
the unique path from node X to node Y.

Victoria wants your help processing Q queries on tree T, where each query contains 2 integers, L and R, such that L<=R.
For each query, she wants to print the number of different paths in T that have a cost, C, in the inclusive range [L, R].

It should be noted that path from some node X to some other node Y is considered same as path from
node Y to X i.e {X, Y} is same as {Y, X}.

Input Format:
The first line contains 2 space-separated integers, N (the number of nodes) and Q (the number of queries), respectively.
Each of the N - 1 subsequent lines contain 3 space-separated integers, U, V, and W, respectively, describing a
bidirectional road between nodes U and V which has weight W.
The Q subsequent lines each contain 2 space-separated integers denoting L and R.

Constraints:
1 <= N,Q <= 10^5
1 <= U,V <= N
1 <= W <= 10^9
1 <= L <= R <= 10^9

Scoring:
1 <= N,Q <= 10^3 for 30% of the test data.
1 <= N,Q <= 10^5 for 100% of the test data.

Output Format:
For each of the Q queries, print the number of paths in T having cost C in the inclusive range {L,R} on a new line.

Sample Input:
5 5
1 2 3
1 4 2
2 5 6
3 4 1
1 1
1 2
2 3
2 5
1 6

Sample Output:
1
3
5
5
10

Explanation:
Q1 : {3, 4}
Q2 : {1, 3}, {3, 4}, {1, 4}
Q3 : {1, 4}, {1, 2}, {2, 4}, {1, 3}, {2, 3}
Q4 : {1, 4}, {1, 2}, {2, 4}, {1, 3}, {2, 3}
...etc.
*/
package com.wissen.hackerrank.nineth.supermaximumcostqueries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.LongStream;

public class Solution {

    class WeightPath {
        public long weight;
        public long path;

        public WeightPath() {
            this.weight = 0;
            this.path = 0;
        }

        public WeightPath(WeightPath weightPath) {
            this.weight = weightPath.weight;
            this.path = weightPath.path;
        }

        public WeightPath(long weight, long path) {
            this.weight = weight;
            this.path = path;
        }
    }

//    /**
//     * STEP 0: Define a Query class
//     * This class provides implementation for storage of query.
//     */
//    class Query {
//        final long upperBound;
//        final long lowerBound;
//
//        public Query(long startNode, long endNode) {
//            this.upperBound = startNode;
//            this.lowerBound = endNode;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Query query = (Query) o;
//            return upperBound == query.upperBound && lowerBound == query.lowerBound;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(upperBound, lowerBound);
//        }
//
//        @Override
//        public String toString() {
//            return "Query{" +
//                    "upperBound=" + upperBound +
//                    ", lowerBound=" + lowerBound +
//                    '}';
//        }
//    }

    /**
     * STEP 1: Define a Edge class
     * This class provides implementation for storage of edge in disjoint set data structure.
     */
    class Edge {
        final long startNode;
        final long endNode;
        final long weight;

        public Edge(long startNode, long endNode, long weight) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return startNode == edge.startNode && endNode == edge.endNode && weight == edge.weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(startNode, endNode, weight);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "startNode=" + startNode +
                    ", endNode=" + endNode +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * STEP 2: Define a DisjointSet generic class
     * This class provides implementation of disjoint set data structure.
     */
    class DisjointSet<T> {
        private final WeightPath weightPath;
        private final List<WeightPath> weightPaths;
        private final List<Map<T, Node<T>>> nodeByData;

        public DisjointSet() {
            this.weightPath = new WeightPath();
            this.weightPaths = new ArrayList<>();
            this.weightPaths.add(new WeightPath(0, 0));
            this.nodeByData = new ArrayList<>();
            this.nodeByData.add(new HashMap<>());
            this.nodeByData.add(new HashMap<>());
        }

        /**
         * STEP 3: Define a Node class
         * This class provides implementation for storage of node in disjoint set data structure.
         */
        class Node<T> {
            final T data;
            Node<T> parent;
            long size;

            public Node(T data) {
                this.data = data;
            }
        }

        public Node<T> getNodeByData(final T data) {
            return nodeByData.get(getIndexFromData(data)).get(data);
        }

        private int getIndexFromData(T data) {
            return (Long.parseLong(data.toString()) > Integer.MAX_VALUE) ? 1 : 0;
        }

        /**
         * STEP 4: Define a makeSet() method in DisjointSet class
         * This method takes one record and adds to the disjoint set.
         *
         * @param data the record which needs to be inserted in the disjoint set.
         */
        public void makeSet(T data) {
            Node<T> newNode = new Node<>(data);
            newNode.parent = newNode;
            newNode.size = 1;
            nodeByData.get(getIndexFromData(data)).put(data, newNode);
        }

        /**
         * STEP 5: Define a union() method in DisjointSet class
         * This method takes two record and finds out the group of both the record if both records are from
         * different then both are the groups are merged into one group.
         *
         * @param firstData  the first record for which group needs to be found and merged.
         * @param secondData the second record for which group needs to be found and merged.
         */
        public void union(final T firstData, final T secondData, long weight) {
            weightPath.weight = weight;
            Node<T> firstNode = nodeByData.get(getIndexFromData(firstData)).get(firstData);
            Node<T> secondNode = nodeByData.get(getIndexFromData(secondData)).get(secondData);

            Node<T> firstParent = findSet(firstNode);
            Node<T> secondParent = findSet(secondNode);

            if (firstParent.data != secondParent.data) {
                weightPath.path += (firstParent.size * secondParent.size);
                secondParent.parent = firstParent.parent;
                firstParent.size += secondParent.size;
                secondParent.size = 0;
            }
            this.weightPaths.add(new WeightPath(weightPath));
        }

        /**
         * STEP 6: Define a findSet() method in DisjointSet class
         * This method takes a node as input and finds the root node for the same input node.
         *
         * @param node the node for which root node needs to be found.
         */
        public Node<T> findSet(Node<T> node) {
            Node<T> parent = node.parent;
            if (parent == node) {
                return parent;
            }
            node.parent = findSet(node.parent);
            return node.parent;
        }

        public List<WeightPath> getWeightPaths() {
            return weightPaths;
        }
    }

    class BinarySearch<T> {

        private final List<T> elements;
        private final Comparator<T> comparator;

        public BinarySearch(List<T> elements, Comparator<T> comparator) {
            this.elements = elements;
            this.comparator = comparator;
        }

        public int search(T element) {
            int compare = 0;
            int left = 0;
            int right = elements.size() - 1;
            int mid = 0;
            while (left + 1 < right) {
                mid = (left + right) >>> 1;
                compare = this.comparator.compare(element, elements.get(mid));
                if (compare > 0) {
                    left = mid;
                } else {
                    right = mid;
                }
            }
            int result = 0;
            if (this.comparator.compare(elements.get(right), element) <= 0) {
                result = right;
            } else {
                result = left;
            }
            return result;
        }
    }

    class SuperMaximumCostQueries {
        private final long nodeCount;
        private List<Edge> sortedEdges;
        private final DisjointSet<Long> edgeDisjointSet;
        private Comparator<Edge> edgeComparator = (o1, o2) -> Long.compare(o1.weight, o2.weight);
        private Comparator<WeightPath> weightComparator = (o1, o2) -> Long.compare(o1.weight, o2.weight);

        public void addEdge(Edge edge) {
            sortedEdges.add(edge);
        }

        public SuperMaximumCostQueries(long edgesCount) {
            this.nodeCount = edgesCount + 1;
            this.sortedEdges = new ArrayList<>();
            this.edgeDisjointSet = new DisjointSet<>();
            LongStream.rangeClosed(1, nodeCount).forEach(this.edgeDisjointSet::makeSet);
        }

        public void processEdges() {
            sortedEdges.sort(edgeComparator);
            for (int i = 0; i < sortedEdges.size(); i++) {
                this.edgeDisjointSet.union(sortedEdges.get(i).startNode, sortedEdges.get(i).endNode,
                        sortedEdges.get(i).weight);
            }
            this.edgeDisjointSet.getWeightPaths().sort(weightComparator);
        }

        public void executeQuery(long lowerBound, long upperBound) {
            executeQuery(lowerBound, upperBound, this.edgeDisjointSet.getWeightPaths());
        }

        private void executeQuery(long lowerBound, long upperBound, List<WeightPath> processedOutput) {
            BinarySearch<WeightPath> weightPathBinarySearch = new BinarySearch<>(processedOutput, weightComparator);
            long lowerBoundPath = processedOutput.get(weightPathBinarySearch.search(
                    new WeightPath(lowerBound - 1, 0))).path;
            long upperBoundPath = processedOutput.get(weightPathBinarySearch.search(
                    new WeightPath(upperBound, 0))).path;
            long result = upperBoundPath - lowerBoundPath;
            System.out.println(result);
        }
    }

    public void init(Scanner scanner) {
        long nodeCount = scanner.nextLong();
        long queryCount = scanner.nextLong();
        SuperMaximumCostQueries superMaximumCostQueries = new SuperMaximumCostQueries((nodeCount - 1));

        for (int i = 0; i < nodeCount - 1; i++) {
            long startNode = scanner.nextLong();
            long endNode = scanner.nextLong();
            long weight = scanner.nextLong();
            superMaximumCostQueries.addEdge(new Edge(startNode, endNode, weight));
        }

        superMaximumCostQueries.processEdges();

        for (int i = 0; i < queryCount; i++) {
            long lowerBound = scanner.nextLong();
            long upperBound = scanner.nextLong();
            superMaximumCostQueries.executeQuery(lowerBound, upperBound);
        }
    }

    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Solution().init(SCANNER);
    }
}