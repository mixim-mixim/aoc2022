package de.mixim.aoc.day4;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    record Elf(Integer no, Integer calories) {
    }

    record SectionRange(int min, int max) {
        public boolean containsFullIn(SectionRange other) {
            return min >= other.min && max <= other.max;
        }
        public boolean overlap(SectionRange other) {
            return (min >= other.min && min <= other.max)
                    || (max >= other.min && max <= other.max)
                    || (min >= other.min && max <= other.max) ;
        }
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day4.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        System.out.println("Result: " + doPartTwo(lnr));
    }

    private static int doPartTwo(LineNumberReader lnr) {
        List<List<SectionRange>> rangesTuples = lnr.lines().map(l -> {
            return Arrays.stream(l.split(",")).toList().stream().map(range -> {
                String[] split = range.split("-");
                return new SectionRange(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]));
            }).toList();
        }).toList();
        return rangesTuples.stream().filter(tuple -> {
            SectionRange sectionRange0 = tuple.get(0);
            SectionRange sectionRange1 = tuple.get(1);
            return sectionRange0.overlap(sectionRange1)
                     || sectionRange1.overlap(sectionRange0);
        }).toList().size();
    }

    private static int doPartOne(LineNumberReader lnr) {
        List<List<SectionRange>> rangesTuples = lnr.lines().map(l -> {
            return Arrays.stream(l.split(",")).toList().stream().map(range -> {
                String[] split = range.split("-");
                return new SectionRange(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]));
            }).toList();
        }).toList();
        return rangesTuples.stream().filter(tuple -> {
            SectionRange sectionRange0 = tuple.get(0);
            SectionRange sectionRange1 = tuple.get(1);
            return sectionRange0.containsFullIn(sectionRange1);
                    // || sectionRange1.containsFullIn(sectionRange0);
        }).toList().size();
    }
}