package org.example.demo.huaweiod;

import java.util.*;

public class TestAILight {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Light[] lights = new Light[n];
        for (int i=0; i<n; i++) {
            int id = scanner.nextInt();
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            lights[i] = new Light(id, (x1+x2)/2, (y1+y2)/2, (x2-x1)/2);
        }
        Arrays.sort(lights, (a, b) -> a.y - b.y);
        StringJoiner stringJoiner = new StringJoiner(" ");
        Light base = lights[0];
        List<Light> temp = new ArrayList<>();
        temp.add(base);
        for (int i=1; i<n; i++) {
            Light light = lights[i];
            if (light.y-base.y <= base.r) {
                temp.add(light);
            } else {
                temp.sort((a, b) -> a.x - b.x);
                temp.forEach(a -> stringJoiner.add(a.id + ""));
                temp.clear();
                base = light;
                temp.add(light);
            }
        }
        if (temp.size() > 0) {
            temp.sort((a, b) -> a.x - b.x);
            temp.forEach(a -> stringJoiner.add(a.id + ""));
        }
        System.out.println(stringJoiner);
    }
}

class Light{
    int id;
    int x;
    int y;
    int r;

    public Light (int id, int x, int y, int r) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
