package org.example.demo.huaweiod;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TestBreak {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();//数据的范围
        scanner.nextLine();

        Queue<Integer> queue = new LinkedList<>();//模拟双端队列
        boolean in_order = true;//是否按顺序删除
        int result = 0;//最小的调整顺序次数

        for (int i = 0; i < 2 * number; i++) {
            String input_str = scanner.nextLine();
            String[] operation = input_str.split(" ");
            if (operation[0].equals("head")) {//从头部添加元素
                if (!queue.isEmpty() && in_order) {//不按顺序删除
                    in_order = false;
                }
                queue.add(Integer.parseInt(operation[2]));
            } else if (operation[0].equals("tail")) {//从尾部添加元素
                queue.add(Integer.parseInt(operation[2]));
            } else {//删除元素
                if (queue.isEmpty()) {
                    continue;
                }
                if (!in_order) {//不按顺序删除
                    result++;
                    in_order = true;
                }
                queue.poll();
            }
        }

        System.out.println(result);//输出最小的调整顺序次数
    }
}
