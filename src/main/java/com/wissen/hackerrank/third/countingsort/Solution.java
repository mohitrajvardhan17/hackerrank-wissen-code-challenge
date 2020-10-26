/*
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/countingsort4

Input Format

The first line contains , the number of integer/string pairs in the array .
Each of the next  contains  and , the integers (as strings) with their associated strings.

Constraints


 is even


 consists of characters in the range

Output Format

Print the strings in their correct order, space-separated on one line.

Sample Input

20
0 ab
6 cd
0 ef
6 gh
4 ij
0 ab
6 cd
0 ef
6 gh
0 ij
4 that
3 be
0 to
1 be
5 question
1 or
2 not
4 is
2 to
4 the
Sample Output

- - - - - to be or not to be - that is the question - - - -
Explanation

Below is the list in the correct order. In the array at the bottom, strings from the first half of the original array
were replaced with dashes.

0 ab
0 ef
0 ab
0 ef
0 ij
0 to
1 be
1 or
2 not
2 to
3 be
4 ij
4 that
4 is
4 the
5 question
6 cd
6 gh
6 cd
6 gh

sorted = [['-', '-', '-', '-', '-', 'to'], ['be', 'or'], ['not', 'to'], ['be'], ['-', 'that', 'is', 'the'],
['question'], ['-', '-', '-', '-'], [], [], [], []]
 */
package com.wissen.hackerrank.third.countingsort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class Solution {

    // Complete the countSort function below.
    static void countSort(List<List<String>> result) {
        Map<Integer, StringBuffer> output = new TreeMap<>();
        for(List<String> rec : result) {
            int index = Integer.parseInt(rec.get(0));
            String data = rec.get(1);
            StringBuffer val = output.putIfAbsent(index, new StringBuffer().append(data).append(" "));
            if(val != null) {
                output.put(index, output.get(index).append(data).append(" "));
            }
        }

        for(Map.Entry<Integer, StringBuffer> val : output.entrySet()) {
            System.out.print(val.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());
        int mid = (n >> 1);
        List<List<String>> arr = new ArrayList<>();


        IntStream.range(0, n).forEach(i -> {
            try {
                String input[] = bufferedReader.readLine().split(" ");
                if (i < mid) {
                    input[1] = "-";
                }
                arr.add(Arrays.asList(input));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        countSort(arr);

        bufferedReader.close();
    }
}
