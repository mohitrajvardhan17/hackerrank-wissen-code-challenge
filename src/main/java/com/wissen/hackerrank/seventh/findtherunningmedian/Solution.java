package com.wissen.hackerrank.seventh.findtherunningmedian;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/
Method 1: Insertion Sort
Method 2: Augmented self balanced binary search tree (AVL, RB, etc…)
Method 3: Heaps
+-----------------------------------------------+-----------------------------------+----------------------------------+
|                                               |          Number >= median         |           Number < median        |
+-----------------------------------------------+-----------------------------------+----------------------------------+
| Size of Left Max Heap = Size of Left Max Heap | 1. Add the number in right min    | 1. Add the number in left max    |
|                                               |    heap.                          |    heap.                         |
|                                               | 2. Median is top of right min     | 2. Median is top of left max     |
|                                               |    heap.                          |    heap.                         |
|                                               |                                   |                                  |
|                                               |                                   |                                  |
+-----------------------------------------------+-----------------------------------+----------------------------------+
| Size of Left Max Heap > Size of Left Max Heap | 1. Add the number in right min    | 1. Remove an element from left   |
|                                               |    heap.                          |    max heap and add the same     |
|                                               | 2. Median is average of top of    |    element in right min heap.    |
|                                               |    left max heap and right min    | 2. Add the number in the left    |
|                                               |    heap.                          |    max heap.                     |
|                                               |                                   | 3. Median is average of top of   |
|                                               |                                   |    left max heap and right min   |
|                                               |                                   |    heap.                         |
+-----------------------------------------------+-----------------------------------+----------------------------------+
| Size of Left Max Heap < Size of Left Max Heap | 1. Remove an element from right   | 1. Add the number to left max    |
|                                               |    min heap and add the same      |    heap.                         |
|                                               |    element in left max heap.      | 2. Median is average of top of   |
|                                               | 2. Add the number in the right    |    left max heap and right min   |
|                                               |    min heap.                      |    heap.                         |
|                                               | 3. Median is average of top of    |                                  |
|                                               |    left max heap and right min    |                                  |
|                                               |    heap.                          |                                  |
+-----------------------------------------------+-----------------------------------+----------------------------------+
##############STEPS##############Todo: Complete this.
https://www.youtube.com/watch?v=EcNbRjEcb14&ab_channel=KeertiPurswani

1. The smaller than median is inserted in the left max heap.
2. The greater than median is inserted in the right min heap.
3. The left max heap and right min heap should always be balanced after insertion of number.
4. If heap is balanced then top of heap is median
5. If heap is not balanced then median is average of top of left heap and right heap
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/find-the-running-median
The median of a set of integers is the midpoint value of the data set for which an equal number of integers are
less than and greater than the value. To find the median, you must first sort your set of integers in non-decreasing
order, then:

1. If your set contains an odd number of elements, the median is the middle element of the sorted sample. In the
sorted set {1, 2, 3}, 2 is the median.

2. If your set contains an even number of elements, the median is the average of the two middle elements of the sorted
sample. In the sorted set {1, 2, 3, 4}, (2 + 3) / 2 = 2.5 is the median.

Given an input stream of n integers, you must perform the following task for each ith integer:

1. Add the ith integer to a running list of integers.
2. Find the median of the updated list (i.e., for the first element through the ith element).
3. Print the list's updated median on a new line. The printed value must be a double-precision number scaled to 1
decimal place (i.e., 12.3 format).

Input Format

The first line contains a single integer, n, denoting the number of integers in the data stream.
Each line i of the n subsequent lines contains an integer, i, to be added to your list.

Constraints
1 <= n <= 10^5
0 <= n <= 10^5

Output Format

After each new integer is added to the list, print the list's updated median on a new line as a single double-precision
number scaled to 1 decimal place (i.e., 12.3 format).

Sample Input
6
12
4
5
3
8
7

Sample Output
12.0
8.0
5.0
4.5
5.0
6.0

Explanation

There are n=6 integers, so we must print the new median on a new line as each integer is added to the list:
1. list = {12} , median = 12.0
2. list = {12, 4} -> {4, 12}, median = (12 + 4) / 2 = 8.0
3. list = {12, 4, 5} -> {4, 5, 12}, median = 5.0
4. list = {12, 4, 5, 3} -> {3, 4, 5, 12}, median = (4 + 5) / 2 = 4.5
5. list = {12, 4, 5, 3, 8} -> {3, 4, 5, 8, 12}, median = 5.0
6. list = {12, 4, 5, 3, 8, 7} -> {3, 4, 5, 7, 8, 12}, median = (5 + 7) / 2 = 6.0
*/
public class Solution {
    /**
     * STEP 1: Define a Person class
     * This class provides implementation for finding and print the running median in stream of integer
     */
    static class RunningMedian {
        /**
         * STEP 2: Define a Max Heap for finding Maximum in left subtree.
         */
        private final PriorityQueue<Double> rightMinHeap = new PriorityQueue<>();

        /**
         * STEP 3: Define a Max Heap for finding Maximum in left subtree.
         */
        private final PriorityQueue<Double> leftMaxHeap = new PriorityQueue<>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return -Double.compare(o1, o2);
            }
        });

        /**
         * STEP 4: Define a getMedian() method in RunningMedian class
         * This method takes a number to be inserted and the value of previous median
         * returns a new median after inserting a number in the list.
         *
         * @param number          the number which needs to be inserted before computing median.
         * @param effectiveMedian the previous median value which is initially zero.
         * @return The new median after inserting a number in the list.
         */
        private Double getMedian(final Double number, final Double effectiveMedian) {
            int result = Integer.compare(leftMaxHeap.size(), rightMinHeap.size());
            Double median = effectiveMedian;
            switch (result) {
                case 0:
                    if (number < effectiveMedian) {
                        leftMaxHeap.add(number);
                        median = leftMaxHeap.peek();
                    } else {
                        rightMinHeap.add(number);
                        median = rightMinHeap.peek();
                    }
                    break;
                case 1:
                    if (number < effectiveMedian) {
                        rightMinHeap.add(leftMaxHeap.poll());
                        leftMaxHeap.add(number);
                    } else {
                        rightMinHeap.add(number);
                    }
                    median = (((leftMaxHeap.peek() == null ? 0 : leftMaxHeap.peek()) +
                            (rightMinHeap.peek() == null ? 0 : rightMinHeap.peek())) /
                            2);
                    break;
                case -1:
                    if (number < effectiveMedian) {
                        leftMaxHeap.add(number);
                    } else {
                        leftMaxHeap.add(rightMinHeap.poll());
                        rightMinHeap.add(number);
                    }
                    median = (((leftMaxHeap.peek() == null ? 0 : leftMaxHeap.peek()) +
                            (rightMinHeap.peek() == null ? 0 : rightMinHeap.peek())) / 2);
                    break;
            }
            return median;
        }

        /**
         * STEP 4: Define a printMedian() method in RunningMedian class
         * This method takes a list of number to be inserted and prints the new median after inserting every number in
         * the list.
         * @param numbers the numbers which needs to be inserted before computing median.
         */
        public void printMedian(final List<Integer> numbers) {
            if (numbers == null || numbers.size() == 0) {
                return;
            }
            Double effectiveMedian = 0D;
            for (Integer number : numbers) {
                effectiveMedian = getMedian(number.doubleValue(), effectiveMedian);
                System.out.println(effectiveMedian);
            }
        }
    }

    static void runningMedian(List<Integer> numbers) {
        new RunningMedian().printMedian(numbers);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int aCount = Integer.parseInt(scanner.nextLine().trim());

        List<Integer> numbers = new ArrayList<>(aCount);

        for (int aItr = 0; aItr < aCount; aItr++) {
            int aItem = Integer.parseInt(scanner.nextLine().trim());
            numbers.add(aItem);
        }
        new Solution().runningMedian(numbers);
    }
}

