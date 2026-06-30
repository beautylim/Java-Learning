package org.example.demo.huaweiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestProcesser {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        int k = scanner.nextInt();
        String[] arr = string.replace("[", "").replace("]", "").split(",");
        List<Integer> nums = Arrays.stream(arr).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();

        List<Integer> link1 = new ArrayList<>();
        List<Integer> link2 = new ArrayList<>();

        for (int num: nums) {
            if (num < 4) {
                link1.add(num);
            } else {
                link2.add(num);
            }
        }

        List<Integer> bestLink = null;
        switch (k) {
            case 1:
                bestLink = getBestLink1(link1, link1.size(), link2, link2.size());
                break;
            case 2:
                bestLink = getBestLink2(link1, link1.size(), link2, link2.size());
                break;
            case 4:
                bestLink = getBestLink4(link1, link1.size(), link2, link2.size());
                break;
            case 8:
                bestLink = getBestLink8(link1, link1.size(), link2, link2.size());
                break;
        }

        if (bestLink == null) return;

        System.out.println(generateCombinations(bestLink, k));


    }

    public static List<List<Integer>> generateCombinations(List<Integer> link, int k) {
        List<List<Integer>> ans = new ArrayList<>();
         dfs(link, k, 0, new ArrayList<Integer>(), ans);
         return ans;
    }

    public static List<List<Integer>> dfs(List<Integer> link, int k, int start, List<Integer> temp, List<List<Integer>> ans) {
        if (temp.size() == k) {
            ans.add(new ArrayList<>(temp));
            return ans;
        }

        for (int i=start; i<link.size(); i++) {
            temp.add(link.get(i));
            dfs(link, k, i+1, temp, ans);
            temp.remove(temp.size()-1);
        }
        return ans;
    }

    public static List<Integer> getBestLink1(List<Integer> link1, int size1, List<Integer> link2, int size2) {
        if (size1 == 1) return link1;
        if (size2 == 1) return link2;
        if (size1 == 3) return link1;
        if (size2 == 3) return link2;
        if (size1 == 2) return link1;
        if (size2 == 2) return link2;
        if (size1 == 4) return link1;
        if (size2 == 4) return link2;
        return null;
    }

    public static List<Integer> getBestLink2(List<Integer> link1, int size1, List<Integer> link2, int size2) {
        if (size1 == 2) return link1;
        if (size2 == 2) return link2;
        if (size1 == 4) return link1;
        if (size2 == 4) return link2;
        if (size1 == 3) return link1;
        if (size2 == 3) return link2;
        return null;
    }

    public static List<Integer> getBestLink4(List<Integer> link1, int size1, List<Integer> link2, int size2) {
        if (size1 == 4) return link1;
        if (size2 == 4) return link2;
        return null;
    }

    public static List<Integer> getBestLink8(List<Integer> link1, int size1, List<Integer> link2, int size2) {
        if (size1 == 4 && size2 == 4) {
            List<Integer> ans = new ArrayList<>(8);
            ans.addAll(link1);
            ans.addAll(link2);
        }
        return null;
    }
}
