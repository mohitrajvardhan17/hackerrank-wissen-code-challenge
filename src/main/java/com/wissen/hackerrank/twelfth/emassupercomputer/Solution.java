/*
##############NOTES##############Todo: Complete this.
https://www.geeksforgeeks.org/find-size-of-the-largest-formed-by-all-ones-in-a-binary-matrix/
##############STEPS##############Todo: Complete this.
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/two-pluses
Ema built a quantum computer! Help her test its capabilities by solving the problem below.

Given a grid of size n X m, each cell in the grid is either good or bad.

A valid plus is defined here as the crossing of two segments (horizontal and vertical) of equal lengths. These lengths
must be odd, and the middle cell of its horizontal segment must cross the middle cell of its vertical segment.

Find the two largest valid pluses that can be drawn on good cells in the grid, and return an integer denoting the
maximum product of their areas. In the above diagrams, our largest pluses have areas of 5 and 9. The product of their
areas is 5 X 9 = 45.

Note: The two pluses cannot overlap, and the product of their areas should be maximal.

Function Description
Complete the twoPluses function in the editor below. It should return an integer that represents the area of the two
largest pluses.

twoPluses has the following parameter(s):
1. grid: an array of strings where each string represents a row and each character of the string represents a column of
that row

Input Format
The first line contains two space-separated integers, n and m.
Each of the next n lines contains a string of m characters where each character is either G (good) or B (bad). These
strings represent the rows of the grid. If the yth character in the xth line is G, then (x,y) is a good cell. Otherwise
it's a bad cell.

Constraints
2 ≤ n ≤ 15
2 ≤ m ≤ 15

Output Format
Find 2 pluses that can be drawn on good cells of the grid, and return an integer denoting the maximum product of their
areas.

Sample Input 0
5 6
GGGGGG
GBBBGB
GGGGGG
GGBBGB
GGGGGG

Sample Output 0
5

Sample Input 1
6 6
BGBBGB
GGGGGG
BGBBGB
GGGGGG
BGBBGB
BGBBGB

Sample Output 1
25
Explanation
Here are two possible solutions for Sample 0 (left) and Sample 1 (right):

Explanation Key:

Green: good cell
Red: bad cell
Blue: possible pluses.

For the explanation below, we will refer to a plus of length i as Pi.

Sample 0
There is enough good space to color one P3 plus and one P1 plus. Area(P3) = 5 units, and Area(P1) = 1 unit. The product
of their areas is 5 X 1 = 5.

Sample 1
There is enough good space to color two P3 pluses. Area(P3) = 5 Units. The product of the areas of our two P3 pluses is
5 X 5 = 25.
*/

package com.wissen.hackerrank.twelfth.emassupercomputer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {

    class MaximalPlusSign {
        public class Point {
            private final int x;
            private final int y;

            public Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return x == point.x && y == point.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }

            @Override
            public String toString() {
                return "Point{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }

        private final String[] grid;
        private final int rowSize;
        private final int columnSize;

        private List<Set<Point>> plusSize = null;

        public MaximalPlusSign(String[] grid) {
            this.grid = grid;
            this.rowSize = grid.length;
            this.columnSize = grid[0] == null ? 0 : grid[0].length();
        }

        private List<Set<Point>> getAllSizeOfPlusSign() {
            if (this.plusSize == null) {
                this.plusSize = new ArrayList<>();
            } else {
                return this.plusSize;
            }
            if (this.grid == null) {
                return null;
            }

            for (int row = 0; row < this.rowSize; ++row) {
                for (int col = 0; col < this.columnSize; ++col) {

                    if (this.grid[row].charAt(col) == 'G') {
                        Set<Point> points = new HashSet<>();
                        points.add(new Point(row, col));
                        this.plusSize.add(new HashSet<>(points));
                        int k = 1;
                        while ((row - k >= 0 && this.grid[row - k].charAt(col) == 'G') &&
                                (row + k < rowSize && this.grid[row + k].charAt(col) == 'G') &&
                                (col - k >= 0 && this.grid[row].charAt(col - k) == 'G') &&
                                (col + k < columnSize && this.grid[row].charAt(col + k) == 'G')) {
                            points.add(new Point(row - k, col));
                            points.add(new Point(row + k, col));
                            points.add(new Point(row, col - k));
                            points.add(new Point(row, col + k));
                            k++;
                            this.plusSize.add(new HashSet<>(points));
                        }

                    }
                }
            }
            return this.plusSize;
        }

        public long getProductOfLargestPlusSign() {
            List<Set<Point>> results = getAllSizeOfPlusSign();

            if (results == null || results.isEmpty()) {
                return 0;
            }

            long maxArea = 0;
            for (int row = 0; row < results.size(); ++row) {
                Set<Point> currentPlus = results.get(row);
                for (int col = row + 1; col < results.size(); ++col) {
                    Set<Point> anotherPlus = results.get(col);
                    long currentArea = (long) currentPlus.size() * anotherPlus.size();
                    if (maxArea < currentArea) {
                        Set<Point> intersection = new HashSet<>(currentPlus);
                        intersection.retainAll(anotherPlus);
                        if (intersection.size() == 0) {
                            maxArea = currentArea;
                        }
                    }
                }
            }
            return maxArea;
        }
    }

    public long twoPluses(String[] grid) {
        return new MaximalPlusSign(grid).getProductOfLargestPlusSign();
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Solution solution = new Solution();
        int row = SCANNER.nextInt();
        SCANNER.skip("(\r\n|[\r\n\u2028\u2029\u0085])?");
        int column = SCANNER.nextInt();
        SCANNER.skip("(\r\n|[\r\n\u2028\u2029\u0085])?");
        if (row <= 0 || column <= 0) {
            return;
        }
        String[] grid = new String[row];
        for (int i = 0; i < row; i++) {
            grid[i] = SCANNER.nextLine();
        }
        long result = solution.twoPluses(grid);
        System.out.println(result);
    }
}