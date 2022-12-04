package de.mixim.aoc.day1;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Elf(Integer no, Integer calories) {};

    public static void main(String[] args) throws IOException {

        ClassPathResource input = new ClassPathResource("/input-day1.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        ArrayList<Elf> elves = new ArrayList<>();
        int calories = 0;
        List<String> lines = lnr.lines().toList();
        for (String line : lines) {
            if (line.isEmpty()) {
                elves.add(new Elf(elves.size(), calories));
                calories = 0;
            } else {
                calories += Integer.parseInt(line);
            }
        }
        System.out.println("Max: " + elves.stream().mapToInt(Elf::calories).max().getAsInt());
        elves.sort((e1, e2) -> e2.calories.compareTo(e1.calories));
        int maxTotal = 0;
        for (int i = 0; i < 3; i++) {
            System.out.println( i + " Max: " + elves.get(i).calories);
            maxTotal += elves.get(i).calories;
        }
        System.out.println("3 Max total: " + maxTotal);

    }
}