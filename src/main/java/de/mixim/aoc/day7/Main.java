package de.mixim.aoc.day7;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static class File {
        File parent;
        String name;
        List<File> files;
        int size;
        boolean isDirectory;

        public File(File parent, String name, List<File> files, int size, boolean isDirectory) {
            this.parent = parent;
            this.name = name;
            this.files = files;
            this.size = size;
            this.isDirectory = isDirectory;
        }

        public String name() {
            return name;
        }

        public List<File> files() {
            return files;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public void addChild(File f) {
            files.add(f);
        }

        public Integer size() {
            return size;
        }

        public File parent() {
            return parent;
        }

        int getDirectorySize() {
            int dirSize = isDirectory
                    ? files.stream().mapToInt(f -> f.getDirectorySize()).sum()
                    : size;
            /* if (isDirectory)
                System.out.printf("%s -> %d%n", name, dirSize);
            */
            return dirSize;
        }
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource input = new ClassPathResource("/input-day7.txt");
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(input.getInputStream()));
        HashMap<File, Integer> fileSizeMap = new HashMap();
        File root = buildTree(lnr, fileSizeMap);

        System.out.println("Tree: ");
        dumpDir(root, "", fileSizeMap);
        List<File> dirs = new ArrayList<>();
        findDirs(root, dirs);
        List<File> dirsUnder100000 = new ArrayList<>();
        findDirsUnder(root, dirsUnder100000, 100000);
        List<File> dirsOver = new ArrayList<>();
        findDirsMore(root, dirsOver, root.getDirectorySize() - 30000000);
        System.out.printf("Root: %d%n", root.getDirectorySize());
        System.out.printf("To free: %d%n", root.getDirectorySize() - 30000000);
        dirsOver.stream().forEach(d -> {
            // System.out.printf("%s -> %d%n", d.name, d.getDirectorySize());
        });
        int rootSize = root.getDirectorySize();
        List<File> resList = fileSizeMap.keySet().stream().filter(k -> 70000000 - (rootSize - k.getDirectorySize()) > 30000000).sorted(
                Comparator.comparing(File::getDirectorySize).reversed()
        ).distinct().toList();
        resList
                .forEach(f -> System.out.printf("%s = %d%n".formatted(f.name, f.getDirectorySize())));
        // resList.get(0).getDirectorySize();
        // System.out.printf("Sum: %d%n", dirsUnder100000.stream().mapToInt(d -> d.getDirectorySize()).sum());
        List<File> sortedEntries = resList.stream()
                .sorted(Comparator.comparingInt(File::getDirectorySize)).toList();
        sortedEntries
                .forEach(e -> System.out.printf("%s - %d%n".formatted(e.name, e.getDirectorySize())));
        System.out.printf("Res %d%n", 70000000 - rootSize + sortedEntries.get(0).getDirectorySize());
    }

    private static void findDirsMore(File dir, List<File> dirsOver, int min) {
        int directorySize = dir.getDirectorySize();
        if (directorySize >= min && !dirsOver.contains(dir)) {
            dirsOver.add(dir);
        }
        dir.files.stream().filter(f -> f.isDirectory).forEach(f ->
                findDirsMore(f, dirsOver, min));
    }

    private static void findDirsUnder(File dir, List<File> dirsUnder100000, int maxSize) {
        int directorySize = dir.getDirectorySize();
        if (directorySize <= maxSize && !dirsUnder100000.contains(dir)) {
            dirsUnder100000.add(dir);
        }
        dir.files.stream().filter(f -> f.isDirectory).forEach(f ->
                findDirsUnder(f, dirsUnder100000, maxSize));
    }

    private static void findDirs(File parent, List<File> dirs) {
        List<File> dirChilds = parent.files().stream().filter(File::isDirectory).toList();
        dirs.addAll(dirChilds);
        dirChilds.stream().forEach(d -> findDirs(d, dirs));
    }

    public static void dumpDir(File dir, String prefix, HashMap<File, Integer> fileSizeMap) {
        System.out.printf("%s %s (%d)%n", prefix, dir.name(), fileSizeMap.get(dir));
        dir.files().stream().filter(f -> !f.isDirectory()).forEach(f ->
                System.out.printf("%s %s (%d)%n", prefix + "-", f.name(), fileSizeMap.get(f))
        );
        dir.files().stream().filter(File::isDirectory).forEach(f ->
                dumpDir(f, prefix + "\t", fileSizeMap));
    }

    private static File buildTree(LineNumberReader lnr, HashMap<File, Integer> fileSizeMap) throws IOException {
        String line = lnr.readLine();

        File root = new File(null, "root", new ArrayList<>(), 0, true);
        File currentDir = root;
        String addFilesLine = null;
        while((line = addFilesLine == null ? lnr.readLine() : addFilesLine) != null) {
            addFilesLine = null;
            // System.out.printf("LineNr: %d %n",lnr.getLineNumber());
            if (line.startsWith("$ cd ")) {
                currentDir = findDir(currentDir, line.substring("$ cd ".length()));
            } else if (line.startsWith("$ ls")) {
                addFilesLine = addFiles(currentDir, lnr, fileSizeMap);
            } else {
                throw new IllegalArgumentException("Command not fully parsed.");
            }
        }
        return root;
    }

    private static String addFiles(File currentDir, LineNumberReader lnr, HashMap<File, Integer> fileSizeMap) throws IOException {
        String line = null;
        while ((line = lnr.readLine()) != null) {
            if (line.startsWith("dir ")) {
                File dir = new File(currentDir, line.substring("dir ".length()), new ArrayList<>(), 0, true);
                currentDir.addChild(dir);
                fileSizeMap.put(dir, dir.size());
            } else if (Character.isDigit(line.charAt(0))) {
                String[] split = line.split(" ");
                int size = Integer.parseInt(split[0]);
                File file = new File(currentDir, split[1], null, size, false);
                currentDir.addChild(file);
                fileSizeMap.put(file, size);
            } else if (line.startsWith("$")) {
                return line;
            } else
                throw new IllegalArgumentException("Line not parseable. %s".formatted(line));
        }
        return null;
    }

    private static File findDir(File currentDir, String newCurrentDir) {
        if (newCurrentDir.equals(".."))
            return currentDir.parent() != null ? currentDir.parent() : currentDir;
        return currentDir.files().stream()
                .filter(f -> f.isDirectory() && f.name().equals(newCurrentDir))
                .findAny().orElseThrow(() ->
                        new IllegalArgumentException("Current dir %s not contains %s"
                                .formatted(currentDir.name(), newCurrentDir))
                );
    }
}