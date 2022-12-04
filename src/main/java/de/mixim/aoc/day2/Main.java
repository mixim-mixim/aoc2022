package de.mixim.aoc.day2;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Elf(Integer no, Integer calories) {};

    public static void main(String[] args) throws IOException {

        ClassPathResource input = new ClassPathResource("/input-day2.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        int sum =
        lnr.lines().mapToInt(l -> getRound(l.substring(0, 1), l.substring(2))).sum();
        System.out.println("Total: " + sum);
    }

    static int getRound(String enemy, String mine) {
        switch(enemy) {
            case "A": return mine.equals("X") ? 0 + 3 : mine.equals("Y") ? 3 + 1 : 6 + 2;
            case "B": return mine.equals("Y") ? 3 + 2 : mine.equals("Z") ? 6 + 3 : 0 + 1;
            case "C": return mine.equals("Z") ? 6 + 1 : mine.equals("X") ? 0 + 2 : 3 + 3;
            default:
                throw new IllegalArgumentException("" + enemy + " " + mine);
        }
    }
}