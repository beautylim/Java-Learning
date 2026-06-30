package org.example.demo.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestMap {

    static void main() {
        List<Integer> list = List.of(1,2,3,3,3,2);

        Map<Integer, Integer> map = list.stream().collect(Collectors.groupingBy(e -> e, Collectors.summingInt(e -> 1)));

        System.out.println(map);
    }
}
