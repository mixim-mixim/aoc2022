package de.mixim.aoc.day6;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day6.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));

        String line = lnr.readLine();
        System.out.println("First Marker = " + findMarker(line, 14));
    }

    private static int findMarker(String line, int markerLen) {
        for (int i = markerLen; i < line.length(); i++) {
            var marker = line.substring(i - markerLen, i).chars().toArray();
            List<Integer> duplicates = new ArrayList<>();
            for (int j = 0; j < markerLen; j++) {
                if (!duplicates.contains(marker[j]))
                    duplicates.add(marker[j]);
            }
            if (duplicates.size() == markerLen) {
                return i;
            }
        }
        throw new IllegalArgumentException("No marker found");
    }

    private static void move2(String[] stacks, int amount, int from, int to) {
    }

    private static void doPartTwo(LineNumberReader lnr) {

    }

    private static void doPartOne(LineNumberReader lnr) {

    }
}