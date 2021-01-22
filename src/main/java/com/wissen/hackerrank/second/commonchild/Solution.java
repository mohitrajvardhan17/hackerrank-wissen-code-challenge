/*
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/common-child
A string is said to be a child of a another string if it can be formed by deleting 0 or more characters from the other
string. Given two strings of equal length, what's the longest string that can be constructed such that it is a child
of both?

For example, ABCD and ABDC have two children with maximum length 3, ABC and ABD.
They can be formed by eliminating either the D or C from both strings. Note that we will not consider ABCD as
a common child because we can't rearrange characters and ABCD  ABDC.

Sample Input

HARRY
SALLY
Sample Output

 2
Explanation

The longest string that can be formed by deleting zero or more characters from  and  is , whose length is 2.
 */
package com.wissen.hackerrank.second.commonchild;

import java.util.stream.Collectors;

public class Solution {

    static String distinctLetter(String s1) {
        return s1.chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining(""));
    }

    static String commonLetter(String d1, String d2) {
        StringBuffer path = new StringBuffer();
        for (int i = 0; i < d2.length(); i++) {
            if (d1.contains(Character.toString(d2.charAt(i)))) {
                path.append(d2.charAt(i));
            }
        }
        return path.toString();
    }

    static String getLetterPosition(String s1, String d) {
        StringBuffer result = new StringBuffer();
        for (char c : s1.toCharArray()) {
            if (d.contains(Character.toString(c))) {
                result.append(c);
            }
        }
        return result.toString();
    }

    static int[][] lcs(char[] X, char[] Y) {
        int[][] C = new int[X.length + 1][Y.length + 1];
        for (int i = 1; i <= X.length; i++) {
            for (int j = 1; j <= Y.length; j++) {
                if (X[i - 1] == Y[j - 1]) { //i-1,j-1
                    C[i][j] = C[i - 1][j - 1] + 1;
                } else {
                    C[i][j] = Math.max(C[i][j - 1], C[i - 1][j]);
                }
            }
        }
        return C;
    }

    static int lcs(int[][] matrix, char[] X, char[] Y) {
        int x = X.length;
        int y = Y.length;
        int lcs[] = new int[matrix[x][y]];
        int count = matrix[x][y];
        int i = x, j = y;
        while (count > 0) {
            if (matrix[i - 1][j] == matrix[i][j]) {
                // Value came from smaller array1
                i--;
            } else if (matrix[i][j - 1] == matrix[i][j]) {
                // Value came from smaller array2
                j--;
            } else {
                // Value came from the letter at the current index
                lcs[count - 1] = X[i - 1];
                i--;
                j--;
                count--;
            }
        }
        return lcs.length;
    }

    // Complete the commonChild function below.
    static int commonChild(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        String c = commonLetter(distinctLetter(s1), distinctLetter(s2));
        String p1 = getLetterPosition(s1, c);
        String p2 = getLetterPosition(s2, c);
        char c1[] = p1.toCharArray();
        char c2[] = p2.toCharArray();
        int[][] result = lcs(c1, c2);
        return lcs(result, c1, c2);
    }

    public static void main(String[] args) {
        String first = "SHINCHAN";
        String second = "NOHARAAA";
        System.out.println("Result = " + commonChild(first, second));
    }
}
