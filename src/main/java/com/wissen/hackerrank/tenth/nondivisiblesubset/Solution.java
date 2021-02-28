package com.wissen.hackerrank.tenth.nondivisiblesubset;

import java.util.Arrays;
import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
https://www.youtube.com/watch?v=NvuzTl-jTRY&ab_channel=CodingCart
https://www.geeksforgeeks.org/subset-no-pair-sum-divisible-k/
https://cs.stackexchange.com/questions/57873/maximum-subset-pairwise-not-divisible-by-k
##############STEPS##############Todo: Complete this.
1. Create a separate array indexed from 0 to (k - 1) which contains frequency of modulus of each number in the array.
2. Calculate the remainder of all numbers in the array by k and increment the frequency of bucket/index associated with the remainder.
3. If k is even then update the (k/2) index by minimum of f[k/2] and 1.
4. Initialize sum by minimum of 1 or count of numbers giving remainder 0.
5. Choose maximum of count of numbers giving remainder i or k-i
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/non-divisible-subset
Given a set of distinct integers, print the size of a maximal subset of S where the sum of any 2 numbers in S' is not
evenly divisible by k.

For example, the array S = {19, 10, 12, 10, 24, 25, 22} and k = 4. One of the arrays that can be created is
S'[0] = {10, 12, 25}. Another is S'[1] = {19, 22, 24}. After testing all permutations, the maximum length solution
array has 3 elements.

Function Description:
Complete the nonDivisibleSubset function in the editor below. It should return an integer representing the length of
the longest subset of S meeting the criteria.

nonDivisibleSubset has the following parameter(s):
S: an array of integers
k: an integer

Input Format:
The first line contains 2 space-separated integers, n and k, the number of values in S and the non factor.
The second line contains n space-separated integers describing S[i], the unique values of the set.

Constraints:
All of the given numbers are distinct.
1 <= n <= 10^5
1 <= k <= 100
1 <= S[i] <= 10^9
All of the given number are distinct.

Output Format:
Print the size of the largest possible subset (S').

Sample Input:
4 3
1 7 2 4

Sample Output:
3

Explanation:
The sums of all permutations of two elements from S = {1, 7, 2, 4} are:
1 + 7 = 8
1 + 2 = 3
1 + 4 = 5
7 + 2 = 9
7 + 4 = 11
2 + 4 = 6
We see that only S' = {1, 7, 4} will not ever sum to a multiple of k = 3.
*/
public class Solution {
    class NonDivisibleSubset {

        public int getNonDivisibleSubset(int K, int[] arr) {
            // Array for storing frequency of modulo values
            int f[] = new int[K];
            Arrays.fill(f, 0);

            // Fill frequency array with values modulo K
            for (int i = 0; i < arr.length; i++) {
                f[arr[i] % K]++;
            }

            // if K is even, then update f[K/2]
            if (K % 2 == 0) {
                f[K / 2] = Math.min(f[K / 2], 1);// if f[K/2] = 0 then 1 else no change required
            }

            // Initialize result by minimum of 1 or count of numbers giving remainder 0
            int res = Math.min(f[0], 1);// if f[0] = 0 then f[0] = 0 else no change required

            // Choose maximum of count of numbers giving remainder i or K-i
            for (int i = 1; i <= K / 2; i++) {
                res += Math.max(f[i], f[K - i]);// if the two remainder deviates from k/2 by equal distance.
            }

            return res;
        }
    }

    public void init(Scanner SCANNER) {
        int n = SCANNER.nextInt();
        int k = SCANNER.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = SCANNER.nextInt();
        }
        System.out.println(new NonDivisibleSubset().getNonDivisibleSubset(k, numbers));
    }

    private final static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Solution().init(SCANNER);
    }
}
