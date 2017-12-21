package com.github.crab2died.retty.common;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class WeightUtil {

    public static String randomWithWeight(List<String[]> list) {

        int total = 0;//总权重
        //以权重区间段的后面的值作为key存当前信息
        TreeMap<Integer, String> map = new TreeMap<>();
        for (String[] array : list) {
            total += Integer.parseInt(array[0]);
            map.put(total, array[1]);
        }

        int random = (int) (Math.random() * total);
        Integer key = map.ceilingKey(random);
        return map.get(key);
    }

    public static void main(String... args) {

        List<String[]> list = new ArrayList<>();
        String[] s1 = {"0", "100权重"};
        list.add(s1);
        String[] s2 = {"2", "200权重"};
        list.add(s2);
        String[] s3 = {"3", "300权重"};
        list.add(s3);
        String[] s4 = {"4", "400权重"};
        list.add(s4);
        int _1 = 0, _2 = 0, _3 = 0, _4 = 0;
        for (int i = 0; i < 10000; i++) {
            String rel = randomWithWeight(list);
            if (rel.equals(s1[1])) {
                _1++;
            }
            if (rel.equals(s2[1])) {
                _2++;
            }
            if (rel.equals(s3[1])) {
                _3++;
            }
            if (rel.equals(s4[1])){
                _4++;
            }
        }
        System.out.println(s1[1] + " : " + _1);
        System.out.println(s2[1] + " : " + _2);
        System.out.println(s3[1] + " : " + _3);
        System.out.println(s4[1] + " : " + _4);
    }
}
