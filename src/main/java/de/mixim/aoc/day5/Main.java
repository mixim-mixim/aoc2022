package de.mixim.aoc.day5;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Main {
    record Elf(Integer no, Integer calories) {
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day5.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));

        String[] stacks = new String[9];
        stacks[0] ="RHMPZ";
        stacks[1] ="BJCP";
        stacks[2] ="DCLGHNS";
        stacks[3] ="LRSQDMTF";
        stacks[4] ="MZBTQPSF";
        stacks[5] ="GBZSFT";
        stacks[6] ="VRN";
        stacks[7] ="MCVDTLGP";
        stacks[8] ="LMFJNQW";

        lnr.lines().forEach(l -> {
            String[] split = l.split(" ");
            int amount = Integer.parseInt(split[1]);
            int from = Integer.parseInt(split[3]);
            int to = Integer.parseInt(split[5]);

            move2(stacks, amount, from - 1, to - 1);
        });
        String result = "";
        for (String stack : stacks) {
            result = result.concat(stack.substring(0, 1));
        }
        System.out.println("Result: " + result);
    }

    private static void move(String[] stacks, int amount, int from, int to) {
        String move = stacks[from].substring(0, amount);
        System.out.println("Move: " + move + " Stack to: " + stacks[to]);
        stacks[from] = stacks[from].substring(amount);
        for (int i = 0; i < move.length(); i++) {
            stacks[to] = move.substring(i, i + 1) + stacks[to];
        }
        System.out.println("Move: " + move + " Stack to: " + stacks[to]);
    }

    private static void move2(String[] stacks, int amount, int from, int to) {
        String fromStr = stacks[from].substring(0, amount);
        stacks[from] = stacks[from].substring(amount);
        stacks[to] = fromStr + stacks[to];
    }

    private static void doPartTwo(LineNumberReader lnr) {

    }

    private static void doPartOne(LineNumberReader lnr) {

    }
}