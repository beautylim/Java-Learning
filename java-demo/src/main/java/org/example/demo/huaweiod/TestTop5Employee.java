package org.example.demo.huaweiod;

import java.util.*;

public class TestTop5Employee {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int employeeNumber = scanner.nextInt();
        int monthDay = 30;
        int[] dayNumbers = new int[monthDay];
        for (int i=0; i < monthDay; i++) {
            dayNumbers[i] = scanner.nextInt();
        }

        List<List<Integer>> attendances = new ArrayList<>(30);

        for (int i=0; i<monthDay; i++) {
            List<Integer> attendance = new ArrayList<>(dayNumbers[i]);
            for (int j=0; j<dayNumbers[i]; j++) {
                attendance.add(scanner.nextInt());
            }
            attendances.add(attendance);
        }

        Map<Integer, Integer> map = new HashMap<>();

        for (List<Integer> attendance : attendances) {
            for (int emplyee: attendance) {
                map.put(emplyee, map.getOrDefault(emplyee, 0) +1);
            }
        }

        int[][] record = new int[employeeNumber][2];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            record[entry.getKey()] = new int[] {entry.getKey(), entry.getValue()};
        }
        Arrays.sort(record, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o2[1] - o1[1];
            }
        });

        for (int i=0; i<5; i++) {
            System.out.println(record[i][0] + record[i][1]);
        }
    }
}
