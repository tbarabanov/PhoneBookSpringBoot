package com.kohls.cpe.darkside;/* Dynamic Programming Java implementation of Coin
Change problem */

import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.fill;

class TestMain {

    static abstract class A {
        abstract int handle(String s);
    }

    static class B extends A {
        @Override
        int handle(String s) {
            return 0;
        }
    }


    static long cw(int weigths[], int N) {

        int ways[] = new int[N + 1];
        ways[0] = 1;

        for (int i = 1; i < ways.length; i++) {
            for (int k = 0; k < weigths.length; k++) {
                if (i - weigths[k] >= 0)
                    ways[i] += ways[i - weigths[k]];
            }
        }
        return -1;
    }


    static long countWays(int S[], int m, int n) {
        //Time complexity of this function: O(mn)
        //Space Complexity of this function: O(n)

        // table[i] will be storing the number of solutions
        // for value i. We need n+1 rows as the table is
        // constructed in bottom up manner using the base
        // case (n = 0)
        long[] table = new long[n + 1];

        // Base case (If given value is 0)
        table[0] = 1;

        // Pick all coins one by one and update the table[]
        // values after the index greater than or equal to
        // the value of the picked coin
        for (int i = 0; i < m; i++)
            for (int j = S[i]; j <= n; j++)
                table[j] += table[j - S[i]];

        return table[n];
    }


    static int minCoins(int coins[], int sum) {


        int table[] = new int[sum + 1];

        fill(table, Integer.MAX_VALUE);
        table[0] = 0;

//        int first[] = new int[sum + 1];


        for (int w : coins) {
            for (int c = 0; c <= sum; c++)
                if (c - w > 0)
                    table[c] = min(table[c], table[c - w] + 1);
        }

        return table[sum];
    }


    static long findMinWeightsCount(int S[], int m, int n) {

        int[] table = new int[n + 1];
        fill(table, Integer.MAX_VALUE);

        table[0] = 0;
        int first[] = new int[n + 1];

        for (int i = 0; i < m; i++)
            for (int j = S[i]; j <= n; j++) {
                if (table[j] > table[j - S[i]] + 1) {
                    table[j] = table[j - S[i]] + 1;
                    first[j] = S[i];
                }
            }

        while (n > 0) {
            System.out.println(first[n]);
            n -= first[n];
        }

        return table[n];
    }

    static int longestGrowingSubSequence(int S[], int m) {

        int length[] = new int[m];

        for (int i = 0; i < m; i++) {
            length[i] = 1;
            for (int j = 0; j < i; j++)
                if (S[i] > S[j])
                    length[i] = max(length[i], length[j] + 1);
        }
        return Arrays.stream(length).max().getAsInt();
    }


    static int maxWeight(int n) {

        int table[][] = new int[n + 1][n + 1];

        // todo: fill the table

        // 0 0 0 0 0
        // 0 1 5 6 8
        // 0 3 6 8 9
        // 0 77 8 11 0
        // 0 5 2 2 2

        for (int x = 1; x < n; x++)
            for (int y = 1; y < n; y++)
                table[x][y] = table[x][y] + max(table[x - 1][y], table[x][y - 1]);

        return table[n][n];
    }


    static boolean[] allSum(int weights[], int m) {

        // 0 1 2 3 4 5 6 7 8 9 10 11 12
        boolean possible[] = new boolean[m + 1];

        //{1, 3, 3, 5},

        possible[0] = true;

        for (int k = 0; k < weights.length; k++)
            for (int x = m - weights[k]; x >= 0; x--)
                possible[x + weights[k]] |= possible[x];

        return possible;
    }


    // Driver Function to test above function
    public static void main(String args[]) {
//        int arr[] = {2, 5, 3, 6};
//        int m = arr.length;
//        int n = 10;
//        System.out.println(countWays(arr, m, n));
//        int arr[] = {1, 2, 5};
//        int m = arr.length;
//        int n = 12;
//        System.out.println(findMinWeightsCount(arr, m, n));

//        int arr[] = {6, 2, 5, 1, 7, 4, 8, 3};
//        int m = arr.length;
//        System.out.println(longestGrowingSubSequence(arr, m));

        System.out.println(Arrays.toString(allSum(new int[]{1, 3, 3, 5}, 12)));
    }
}
// This code is contributed by Pankaj Kumar
