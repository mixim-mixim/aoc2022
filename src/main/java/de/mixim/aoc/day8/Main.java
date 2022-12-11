package de.mixim.aoc.day8;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day8.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        List<String> lines = lnr.lines().toList();
        byte[][] matrix = readMatrix(lines);
        long[][] resultMatrix = new long[matrix.length][matrix.length];

        calculateBestPlace(matrix, resultMatrix);

        // dumpMatrix(resultMatrix, matrix);
        long max = findMax(resultMatrix);
        System.out.printf("Max: %d%n", max);
    }

    private static long findMax(long[][] resultMatrix) {
        long max = 0;
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix.length; j++) {
                max = Math.max(max, resultMatrix[i][j]);
            }
        }
        return max;
    }

    private static void dumpMatrix(long[][] matrix, byte[][] bytes) {
        // if (matrix.length > 0)
        //    return;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("[%d-%d]".formatted(matrix[i][j], bytes[i][j]));
            }
            System.out.printf("%n");
        }
    }

    private static void calculateBestPlace(byte[][] matrix, long[][] resultMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                byte top = find(matrix, i, j, 0, -1);
                byte right = find(matrix, i, j, 1, 0);
                byte down = find(matrix, i, j, 0, 1);
                byte left = find(matrix, i, j, -1, 0);
                resultMatrix[i][j] = top * right * down * left;
            }
        }
    }

    private static byte find(byte[][] matrix, int x, int y, int plusX, int plusY) {
        byte myHeight = matrix[x][y];
        byte treeCount = 0;
        do {
            if(isEdge(x , y, matrix.length)) {
                return treeCount;
            }
            x += plusX;
            y += plusY;
            treeCount++;

        } while(matrix[x][y] < myHeight);
        return treeCount;
    }

    private static boolean isEdge(int x, int y, int matrixLength) {
        return x == 0 || y == 0 || x >= matrixLength - 1 || y >= matrixLength - 1;
    }

    public static void mainDay1(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day8.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        List<String> lines = lnr.lines().toList();
        byte[][] matrix = readMatrix(lines);
        byte[][] visibleMatrix = new byte[matrix.length][matrix.length];


        // from left
        for (int i = 0; i < matrix.length; i++) {
            byte[] visibles = findVisibles(matrix[i]);
            mapVisibles(visibles, visibleMatrix[i]);
        }
        dumpMatrix(matrix);
        System.out.printf("%n");
        dumpMatrix(visibleMatrix);
        System.out.printf("%n");
        System.out.printf("%n");


        // rotate clockwise matrix
        matrix = rotate(matrix);
        visibleMatrix = rotate(visibleMatrix);

        // from down
        for (int i = 0; i < matrix.length; i++) {
            byte[] visibles = findVisibles(matrix[i]);
            mapVisibles(visibles, visibleMatrix[i]);
        }
        dumpMatrix(matrix);
        System.out.printf("%n");
        dumpMatrix(visibleMatrix);
        System.out.printf("%n");
        System.out.printf("%n");

        // rotate clockwise matrix
        matrix = rotate(matrix);
        visibleMatrix = rotate(visibleMatrix);

        // from right
        for (int i = 0; i < matrix.length; i++) {
            byte[] visibles = findVisibles(matrix[i]);
            mapVisibles(visibles, visibleMatrix[i]);
        }
        dumpMatrix(matrix);
        System.out.printf("%n");
        dumpMatrix(visibleMatrix);
        System.out.printf("%n");
        System.out.printf("%n");

        // rotate clockwise matrix
        matrix = rotate(matrix);
        visibleMatrix = rotate(visibleMatrix);

        // from top
        for (int i = 0; i < matrix.length; i++) {
            byte[] visibles = findVisibles(matrix[i]);
            mapVisibles(visibles, visibleMatrix[i]);
        }
        dumpMatrix(matrix);
        System.out.printf("%n");
        dumpMatrix(visibleMatrix);
        System.out.printf("%n");
        System.out.printf("%n");


        int visible = countVisible(visibleMatrix);
        System.out.printf("Visible: %d%n", visible);
    }

    private static int countVisible(byte[][] visibleMatrix) {
        int visible = 0;
        for (int i = 0; i < visibleMatrix.length; i++) {
            for (int j = 0; j < visibleMatrix[i].length; j++) {
                visible += visibleMatrix[i][j];
            }
        }
        return visible;
    }

    private static void dumpMatrix(byte[][] matrix) {
        if (matrix.length > 0)
            return;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("[%d]".formatted(matrix[i][j]));
            }
            System.out.printf("%n");
        }
    }

    private static byte[][] rotate(byte[][] matrix) {
        byte[][] result = new byte[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[matrix.length - 1 - j][matrix.length - 1 - i] = matrix[i][j];
            }
        }
        for (int i = 0; i < matrix.length / 2; i++) {
            byte[] row = result[matrix.length - 1 - i];
            result[matrix.length - 1 - i] = result[i];
            result[i] = row;
        }
        return result;
    }

    private static void mapVisibles(byte[] visibles, byte[] visibleMatrix) {
        for (int i = 0; i < visibles.length; i++) {
            visibleMatrix[i] = (byte) Math.max(visibles[i], visibleMatrix[i]);
        }
    }

    static byte[] findVisibles(byte[] input) {
        byte[] result = new byte[input.length];
        byte max = 0;
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) ((i == 0 ? input[i] >= max : input[i] > max) ? 1 : 0);
            max = (i == 0 ? input[i] >= max : input[i] > max) ? input[i] : max;
        }
        return result;
    }

    private static byte[][] readMatrix(List<String> lines) {
        byte[][] result = new byte[lines.size()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            int[] ints = lines.get(i).chars().map(c -> Integer.parseInt(Character.valueOf((char) c).toString())).toArray();
            for (int j = 0; j < ints.length; j++) {
                result[i][j] = (byte) ints[j];
            }
        }
        return result;
    }

}