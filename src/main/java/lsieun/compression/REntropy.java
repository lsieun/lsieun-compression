package lsieun.compression;

import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

public class REntropy {
    public static double getShannonEntropy(String s) {
        int count = 0;
        Map<Character, Integer> map = new HashMap<>();

        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (map.containsKey(ch)) {
                map.put(ch, map.get(ch) + 1);
            }
            else {
                map.put(ch, 1);
            }
            ++count;
        }

        double entropy = 0.0;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            double p = (double) entry.getValue() / count;
            entropy += p * log2(p);
        }
        return -entropy;
    }

    private static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    public static void main(String[] args) {
        String[] array = {
                "1223334444",
                "1223334444555555555",
                "122333",
                "1227774444",
                "aaBBcccDDDD",
                "1234567890abcdefghijklmnopqrstuvwxyz",
                "Rosetta Code",
        };

        for (String str : array) {
            double entropy = REntropy.getShannonEntropy(str);
            System.out.printf("Shannon entropy of %40s: %.12f%n", "\"" + str + "\"", entropy);
        }
    }
}