package org.example.demo;

import java.io.*;

public class TestCode {

    public static void main(String[] args) {
        System.out.println(test("abba"));
        System.out.println(test("abbc"));
    }

    public static boolean test(String string) {
        int n = string.length();

        boolean[][] dp = new boolean[n][n];

        for (int i=0; i<n; i++) {
            dp[i][i] = true;
        }

        for (int L=2; L<=n; L++) {
            for (int i=0; i<n; i++) {
                int j = i + L -1;
                if (j >= n) {
                    continue;
                }

                char ic = string.charAt(i);
                char jc = string.charAt(j);
                if (ic == jc) {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }
            }
        }
        return dp[0][n-1];
    }
}
