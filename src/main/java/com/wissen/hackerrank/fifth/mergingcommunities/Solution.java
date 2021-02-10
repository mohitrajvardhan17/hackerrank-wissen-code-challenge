package com.wissen.hackerrank.fifth.mergingcommunities;

import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
##############STEPS##############Todo: Complete this.
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
     * STEP 1: Define a Person class
     * This class provides implementation for storage of a parent node of a person as parent and number of child
     * node as size.
     */
    static class Person {
        int parent = -1;
        int size = 1;
    }

    /**
     * STEP 2: Define a CommunityQueryProcessor class
     * This class provides implementation for processing community query such as the merge query ond the size query.
     */
    static class CommunityQueryProcessor {
        private final Person[] people;

        /**
         * STEP 3: Define a CommunityQueryProcessor class constructor
         * This constructor creates the memory with required number of person element in the people array.
         */
        public CommunityQueryProcessor(int numberOfPeople) {
            people = new Person[numberOfPeople + 1];
        }

        /**
         * STEP 4: Define a initializeCommunity() method in CommunityQueryProcessor class
         * This method initializes all the person element of people array with new person objects.
         */
        public void initializeCommunity(int numberOfPeople) {
            for (int i = 1; i <= numberOfPeople; i++) {
                people[i] = new Person();
            }
        }

        /**
         * STEP 5: Define a processCommunitySizeQueryByPersonIndex() method in CommunityQueryProcessor class
         * This method iterate over the people array and find out the root person index and also flatten the the tree
         * hierarchy.
         * @param personIndex the person index for which root person index is required.
         * @return the index of the root person.
         */
        private int findRootAndAdjustDepth(int personIndex) {
            //Step 1 : Finding the parent for given person.
            int root = personIndex;
            while (people[root].parent != -1) {
                root = people[root].parent;
            }

            //Step 2 : Adjust all the parent of parent as the root person so the depth decrease and search is efficient.
            int person = personIndex;
            while (person != root) {
                int nextParent = people[person].parent;
                people[person].parent = root;
                person = nextParent;
            }
            return root;
        }

        /**
         * STEP 6: Define a processCommunitySizeQueryByPersonIndex() method in CommunityQueryProcessor class
         * This method iterate over the people array and find out the size of the community.
         * @param personIndex the person index for which community size is required.
         * @return the size of community of the person.
         */
        public int processCommunitySizeQueryByPersonIndex(int personIndex) {
            return people[findRootAndAdjustDepth(personIndex)].size;
        }

        /**
         * STEP 7: Define a processCommunityMergeQuery() method in CommunityQueryProcessor class
         * This method iterate over the people array and find out the community of two person if the communities is
         * not same then the communities are merged.
         * @param firstPersonIndex the first person index for which community needs to be merged.
         * @param secondPersonIndex the second person index for which community needs to be merged.
         */
        public void processCommunityMergeQuery(int firstPersonIndex, int secondPersonIndex) {
            int firstPersonRoot = findRootAndAdjustDepth(firstPersonIndex);
            int secondPersonRoot = findRootAndAdjustDepth(secondPersonIndex);
            if (firstPersonRoot != secondPersonRoot) {
                people[secondPersonRoot].parent = firstPersonRoot;
                people[firstPersonRoot].size += people[secondPersonRoot].size;
            }
        }
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
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
}