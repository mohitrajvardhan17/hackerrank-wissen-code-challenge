package com.wissen.hackerrank.fifth.mergingcommunities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input[] = br.readLine().split(" ");
        int people = Integer.parseInt(input[0]);
        int queries = Integer.parseInt(input[1]);
        int[] group = new int[people];
        for (int i = 0; i < group.length; i++) {
            group[i] = 1;
        }
        Map<Integer, Integer> connection = IntStream.range(0, group.length)
                .boxed()
                .collect(Collectors.toMap(k -> (Integer) k, v -> (Integer) v));
        for (int i = 0; i < queries; i++) {
            input = br.readLine().split(" ");
            if ("M".equalsIgnoreCase(input[0].trim())) {
                int firstPerson = Integer.parseInt(input[1]) - 1;
                int secondPerson = Integer.parseInt(input[2]) - 1;
                int firstGroupIndex = connection.get(firstPerson);
                int secondGroupIndex = connection.get(secondPerson);
                if(firstGroupIndex != secondGroupIndex &&
                        (group[firstGroupIndex] != -1 || group[secondGroupIndex] != -1)) {
                    connection.entrySet().stream().filter(e -> e.getValue() == secondGroupIndex).forEach(d -> {
                        connection.put(d.getKey(), firstGroupIndex);
                    });
                    group[firstGroupIndex] = group[firstGroupIndex] + group[secondGroupIndex];
                    group[secondGroupIndex] = 0;
                }
            } else if ("Q".equalsIgnoreCase(input[0].trim())) {
                int index = Integer.parseInt(input[1]) - 1;
                System.out.println(group[connection.get(index)]);
            }
        }
    }
}