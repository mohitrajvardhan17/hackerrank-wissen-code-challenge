/*
##############NOTES##############Todo: Complete this.
https://stackoverflow.com/questions/64085531/solution-logic-to-organizing-containers-of-balls-question-on-hackerrank
##############STEPS##############Todo: Complete this.
1. Calculate the individual sum of the capacity of each container.
2. Calculate the individual sum of each type of ball.
3. Sort the array containing the capacity of each container.
4. Sort the array containing the count of each ball.
5. Compare both the sorted array if same then possible
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/organizing-containers-of-balls

David has several containers, each with a number of balls in it. He has just enough containers to sort each type of
ball he has into its own container. David wants to sort the balls using his sort method.

As an example, David has n = 2 containers and 2 different types of balls, both of which are numbered from 0 to n-1 = 1.
The distribution of ball types per container are described by an n X n matrix of integers, M[container][type]. For
example, consider the following diagram for M = [[1, 4], [2, 3]]:

In a single operation, David can swap two balls located in different containers.

David wants to perform some number of swap operations such that:
 1. Each container contains only balls of the same type.
 2. No two balls of the same type are located in different containers.

You must perform q queries where each query is in the form of a matrix, M. For each query, print Possible on a new line
if David can satisfy the conditions above for the given matrix. Otherwise, print Impossible.

Function Description
Complete the organizingContainers function in the editor below. It should return a string, either Possible or Impossible.

organizingContainers has the following parameter(s):
containter: a two dimensional array of integers that represent the number of balls of each color in each container

Input Format
The first line contains an integer q, the number of queries.

Each of the next q sets of lines is as follows:
 1. The first line contains an integer , the number of containers (rows) and ball types (columns).
 2. Each of the next n lines contains n space-separated integers describing row M[i].

Constraints
1 ≤ q ≤ 10
1 ≤ n ≤ 100
1 ≤ M[container][type] ≤ 10^9

Scoring
For 33% of score, 1 ≤ n ≤ 10.
For 100% of score, 1 ≤ n ≤ 100.

Output Format
For each query, print Possible on a new line if David can satisfy the conditions above for the given matrix. Otherwise,
print Impossible.

Sample Input 0
2
2
1 1
1 1
2
0 2
1 1

Sample Output 0
Possible
Impossible

Sample Input 1

2
3
1 3 1
2 1 2
3 3 3
3
0 2 1
1 1 1
2 0 0
Sample Output 1

Impossible
Possible
*/
package com.wissen.hackerrank.eleventh.organizingcontainersofballs;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.LongStream;

public class Solution {

    private static long getContainerCapacity(long[] container) {
        return LongStream.of(container).sum();
    }

    private static long[] getContainersCapacity(long[][] containers) {
        long[] containersCapacity = new long[containers.length];
        for (int container = 0; container < containers.length; container++) {
            containersCapacity[container] = getContainerCapacity(containers[container]);
        }
        return containersCapacity;
    }

    private static long[] getBallsCount(long[][] containers) {
        long[] ballsCount = new long[containers[0].length];
        for (int container = 0; container < containers.length; container++) {
            for (int ball = 0; ball < containers[0].length; ball++) {
                ballsCount[ball] += containers[container][ball];
            }
        }
        return ballsCount;
    }


    private static boolean compareArrayElement(long[] containersCapacity, long[] ballsCount) {
        if (containersCapacity.length != ballsCount.length) {
            return false;
        }

        for (int i = 0; i < containersCapacity.length; i++) {
            if (containersCapacity[i] != ballsCount[i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSwapSortingPossible(long[][] containers) {
        long containersCapacity[] = getContainersCapacity(containers);
        long ballsCount[] = getBallsCount(containers);
        Arrays.sort(containersCapacity);
        Arrays.sort(ballsCount);
        if (!compareArrayElement(containersCapacity, ballsCount)) {
            return false;
        }
        return true;
    }

    static String organizingContainers(long[][] containers) {
        boolean result = isSwapSortingPossible(containers);
        if (result) {
            return "Possible";
        } else {
            return "Impossible";
        }
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int queryCount = SCANNER.nextInt();
        SCANNER.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        for (int i = 0; i < queryCount; i++) {
            int containerCount = SCANNER.nextInt();
            SCANNER.skip("(\r\n|[\r\n\u2028\u2029\u0085])?");
            int ballCount = containerCount;
            long[][] containerBallMatrix = new long[containerCount][ballCount];
            for (int j = 0; j < containerCount; j++) {
                for (int k = 0; k < ballCount; k++) {
                    containerBallMatrix[j][k] = SCANNER.nextLong();
                    SCANNER.skip("(\r\n|[\r\n\u2028\u2029\u0085])?");
                }
            }
            String result = organizingContainers(containerBallMatrix);
            System.out.println(result);
        }
        SCANNER.close();
    }
}
