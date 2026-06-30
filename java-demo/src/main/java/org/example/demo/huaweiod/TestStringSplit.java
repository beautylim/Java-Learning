package org.example.demo.huaweiod;

import java.util.Scanner;

public class TestStringSplit {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = Integer.parseInt(scanner.nextLine());

        String string = scanner.nextLine();
        String[] arr = string.split("-");
        int n = arr.length;
        String prefix = arr[0];

        StringBuilder sb = new StringBuilder();
        for (int i=1; i<n; i++) {
            sb.append(arr[i]);
        }

        StringBuilder splitSb = new StringBuilder();
        String str = sb.toString();
        for (int i=0; i< str.length(); i++) {
            splitSb.append(str.charAt(i));
            if ((i+1)%k == 0) {
                splitSb.append('-');
            }
        }
        System.out.println(splitSb);

        String[] strArr = splitSb.toString().split("-");
        StringBuilder transBuilder = new StringBuilder();
        for (int i=0; i<strArr.length; i++) {
            String phrase = strArr[i];
            long upperSize = phrase.chars().filter(Character::isUpperCase).count();
            long lowerSize = phrase.chars().filter(Character::isLowerCase).count();
            if (upperSize > lowerSize) {
                phrase = phrase.toUpperCase();
            } else if (lowerSize > upperSize) {
                phrase = phrase.toLowerCase();
            }
            transBuilder.append(phrase).append("-");
        }
        String suffix = transBuilder.toString().substring(0, transBuilder.length()-1);
        System.out.println(prefix+"-"+suffix);
    }
}
