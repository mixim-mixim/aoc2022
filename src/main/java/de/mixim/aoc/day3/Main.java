package de.mixim.aoc.day3;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Elf(Integer no, Integer calories) {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("'A': " + ((int) 'A'));
        ClassPathResource input = new ClassPathResource("/input-day3.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        // int total = doPartOne(lnr);
        int total = doPartTwo(lnr);
        System.out.println("Total: " + total);
    }

    private static int doPartTwo(LineNumberReader lnr) {
        List<String> lines = lnr.lines().toList();
        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < lines.size(); i+=3) {
            ArrayList<String> group = new ArrayList<>();
            group.add(lines.get(i));
            group.add(lines.get(i + 1));
            group.add(lines.get(i + 2));
            groups.add(group);
        }
        return groups.stream().mapToInt(g -> {
            int commonChar = g.get(0).chars().filter(c -> {
                String s2Search = "%s".formatted((char) c);
                boolean contains = g.get(0).contains(s2Search) &&
                        g.get(1).contains(s2Search) && g.get(2).contains(s2Search);
                return contains;
                    /* findItemType(g.get(0), g.get(1)) == c
                            && findItemType(g.get(1), g.get(2)) == c).findFirst().orElseThrow(
                    () ->
                            new IllegalArgumentException("No common c found: %s - %s - %s"
                                    .formatted(g.get(0), g.get(1), g.get(2)))

                     */
            }).findAny().orElseThrow(() -> new IllegalArgumentException("No common c found: %s - %s - %s"
                    .formatted(g.get(0), g.get(1), g.get(2))));
            boolean isLower = Character.isLowerCase((char) commonChar);
            int prio = isLower ? commonChar - 'a' + 1 : commonChar - 'A' + 27;
            System.out.printf("Common '%s' (Prio: %d) found: %s - %s - %s%n",
                    Character.valueOf((char) commonChar), prio, g.get(0), g.get(1), g.get(2));
            return prio;
        }).sum();
    }

    private static int doPartOne(LineNumberReader lnr) {
        return lnr.lines().mapToInt(l -> {
            int mid = l.length() / 2;
            String comp0 = l.substring(0, mid);
            String comp1 = l.substring(mid);
            Character itemType = findItemType(comp0, comp1);
            // itemType.charValue()
            boolean isLower = Character.isLowerCase(itemType);
            int prio = isLower ? itemType.charValue() - 'a' + 1 : itemType.charValue() - 'A' + 27;
            System.out.println("Char: " + itemType + " - Prio: " + prio);
            return prio;
        }).sum();
    }

    private static Character findItemType(String comp0, String comp1) {
        return Character.valueOf((char) comp0.chars().filter(c0 -> comp1.chars().anyMatch(c1 -> c1 == c0)).findFirst().orElseThrow(
                () -> { return new IllegalArgumentException(comp1); }
        ));
    }

}