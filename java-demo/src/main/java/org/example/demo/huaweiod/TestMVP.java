package org.example.demo.huaweiod;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestMVP {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> scores = new ArrayList<>();
        int sum = 0;
        for (int i=0; i<n; i++) {
            scores.add(scanner.nextInt());
            sum += scores.get(i);
        }

        for (int i = 2; i<= n; i++) {
            if (sum %i == 0) {
                boolean[] used = new boolean[scores.size()];
                if (isPossible(scores, used, sum/i, sum)) {
                    break;
                }
            }

        }

    }

    private static boolean isPossible(List<Integer> scores, boolean[] used, int target, int total) {
        // 一共需要分多少组
        int groups = total / target;

        for (int g = 0; g < groups; g++) {
            // 每一组都要凑出 target
            if (!backtrack(scores, used, target, 0)) {
                return false;
            }
        }
        return true;
    }

    // 回溯：凑出一组和为 target
    private static boolean backtrack(List<Integer> scores, boolean[] used, int target, int currentSum) {
        if (currentSum == target) {
            return true;
        }

        for (int i = 0; i < scores.size(); i++) {
            // 没用过 + 不超过目标值
            if (!used[i] && currentSum + scores.get(i) <= target) {
                used[i] = true;
                if (backtrack(scores, used, target, currentSum + scores.get(i))) {
                    return true;
                }
                used[i] = false; // 回溯
            }
        }
        return false;
    }
}
