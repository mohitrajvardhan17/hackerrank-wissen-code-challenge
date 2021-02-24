package com.wissen.hackerrank.fifth.mergingcommunities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
##############STEPS##############Todo: Complete this.
1. Rank of a parent node increases only when both the node parent have same rank.
##############QUESTION##############
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/merging-communities
People connect with each other in a social network. A connection between Person I and Person J is represented as MIJ.
When two persons belonging to different communities connect, the net effect is the merger of both communities which I
 and J belongs to.

At the beginning, there are N people representing N communities. Suppose person 1 and 2 connected and later 2 and 3
connected, then 1,2, and 3 will belong to the same community.

There are two type of queries:
 1. MIJ -> communities containing person I and J merged (if they belong to different communities).
 2. QI -> print the size of the community to which person I belongs.

Input Format :
The first line of input will contain integers N and Q, i.e. the number of people and the number of queries.
The next Q lines will contain the queries.

Constraints :
1 <= N <= 10^5
1 <= Q <= 2 * 10^5

Output Format :
The output of the queries.

Sample Input :
3 6
Q 1
M 1 2
Q 2
M 2 3
Q 3
Q 2

Sample Output :
1
2
3
3

Explanation :
Initial size of each of the community is 1.
 */
public class Solution {

    /**
     * STEP 1: Define a DisjointSet generic class
     * This class provides implementation of disjoint set data structure.
     */
    class DisjointSet<T> {

        private Map<T, Node<T>> nodeByData = new HashMap<>();

        /**
         * STEP 2: Define a Node class
         * This class provides implementation for storage of node in disjoint set data structure.
         */
        public class Node<T> {
            final T data;
            int rank;
            int size;
            Node parent = null;

            public Node(T data) {
                this.data = data;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Node node = (Node) o;
                return rank == node.rank && parent == node.parent && data.equals(node.data);
            }

            @Override
            public int hashCode() {
                return Objects.hash(data, rank, parent);
            }
        }

        /**
         * STEP 3: Define a getNode() method in DisjointSet class
         * This method takes data and returns the associated node in the disjoint set.
         *
         * @param data the data for which node is required.
         * @return The node for the given data or null.
         */
        public Node<T> getNode(T data) {
            return nodeByData.get(data);
        }

        /**
         * STEP 4: Define a makeSet() method in DisjointSet class
         * This method takes one record and adds to the disjoint set.
         *
         * @param data the record which needs to be inserted in the disjoint set.
         */
        public void makeSet(final T data) {
            Node<T> newNode = new Node<T>(data);
            newNode.parent = newNode;
            newNode.rank = 0;
            newNode.size = 1;
            nodeByData.put(data, newNode);
        }

        /**
         * STEP 5: Define a union() method in DisjointSet class
         * This method takes two record and finds out the group of both the record if both records are from
         * different then both are the groups are merged into one group.
         *
         * @param firstData  the first record for which group needs to be found and merged.
         * @param secondData the second record for which group needs to be found and merged.
         */
        public void union(final T firstData, final T secondData) {
            Node<T> firstNode = nodeByData.get(firstData);
            Node<T> secondNode = nodeByData.get(secondData);
            Node<T> firstNodeParent = findSet(firstNode);
            Node<T> secondNodeParent = findSet(secondNode);

            if (firstNodeParent.data == secondNodeParent.data) {
                return;
            }

            if (firstNodeParent.rank >= secondNodeParent.rank) {
                firstNodeParent.size += secondNodeParent.size;
                firstNodeParent.rank = (firstNodeParent.rank == secondNodeParent.rank) ?
                        firstNodeParent.rank + 1 : firstNodeParent.rank;
                secondNodeParent.parent = firstNodeParent;
            } else {
                secondNodeParent.size += firstNodeParent.size;
                firstNodeParent.parent = secondNodeParent;
            }
        }

        /**
         * STEP 6: Define a findSet() method in DisjointSet class
         * This method takes a node as input and finds the root node for the same input node.
         *
         * @param firstNode the node for which root node needs to be found.
         * @return The parent node for the given node.
         */
        public Node<T> findSet(final Node firstNode) {
            Node<T> parent = firstNode.parent;
            if (parent == firstNode) {
                return parent;
            }
            firstNode.parent = findSet(firstNode.parent);
            return firstNode.parent;
        }
    }


    /**
     * STEP 7: Define a CommunityQueryProcessor class
     * This class provides implementation for processing community query such as the merge query ond the size query.
     */
    class CommunityQueryProcessor {

        private final DisjointSet<Integer> disjointSet;

        /**
         * STEP 8: Define a CommunityQueryProcessor class constructor
         * This constructor creates the memory with required number of person element in the people array.
         */
        public CommunityQueryProcessor(int numberOfPeople) {
            this.disjointSet = new DisjointSet<Integer>();
            initializeCommunity(numberOfPeople);
        }

        /**
         * STEP 9: Define a initializeCommunity() method in CommunityQueryProcessor class
         * This method initializes all the person element of people array with new person objects.
         */
        private void initializeCommunity(int numberOfPeople) {
            for (int i = 1; i <= numberOfPeople; i++) {
                disjointSet.makeSet(i);
            }
        }


        /**
         * STEP 10: Define a processCommunitySizeQueryByPersonIndex() method in CommunityQueryProcessor class
         * This method iterate over the people array and find out the size of the community.
         *
         * @param personIndex the person index for which community size is required.
         * @return the size of community of the person.
         */
        public int processCommunitySizeQueryByPersonIndex(int personIndex) {
            return disjointSet.findSet(disjointSet.getNode(personIndex)).size;
        }

        /**
         * STEP 11: Define a processCommunityMergeQuery() method in CommunityQueryProcessor class
         * This method iterate over the people array and find out the community of two person if the communities is
         * not same then the communities are merged.
         *
         * @param firstPersonIndex  the first person index for which community needs to be merged.
         * @param secondPersonIndex the second person index for which community needs to be merged.
         */
        public void processCommunityMergeQuery(int firstPersonIndex, int secondPersonIndex) {
            disjointSet.union(firstPersonIndex, secondPersonIndex);
        }
    }

    public void init(Scanner SCANNER) {
        int people = SCANNER.nextInt();
        int queries = SCANNER.nextInt();
        CommunityQueryProcessor communityQueryProcessor = new CommunityQueryProcessor(people);
        communityQueryProcessor.initializeCommunity(people);
        for (int i = 1; i <= queries; i++) {
            String command = SCANNER.next();
            if ("M".equalsIgnoreCase(command)) {
                int firstPersonIndex = SCANNER.nextInt();
                int secondPersonIndex = SCANNER.nextInt();
                communityQueryProcessor.processCommunityMergeQuery(firstPersonIndex, secondPersonIndex);
            } else if ("Q".equalsIgnoreCase(command)) {
                int personIndex = SCANNER.nextInt();
                int result = communityQueryProcessor.processCommunitySizeQueryByPersonIndex(personIndex);
                System.out.println(result);
            }
        }
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Solution().init(SCANNER);
    }
}