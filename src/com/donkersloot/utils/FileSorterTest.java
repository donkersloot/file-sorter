package com.donkersloot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileSorterTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting FileSorter automated tests...");

        Path tempDir = Files.createTempDirectory("file-sorter-test");
        try {
            Path inputDir = tempDir.resolve("input");
            Path outputDir = tempDir.resolve("output");
            Files.createDirectories(inputDir);
            Files.createDirectories(outputDir);

            // Create test file 1: Jan 15, 2024
            Path file1 = inputDir.resolve("test1.txt");
            Files.writeString(file1, "Test content 1");
            setLastModified(file1, 2024, 1, 15);

            // Create test file 2: March 10, 2025
            Path file2 = inputDir.resolve("test2.txt");
            Files.writeString(file2, "Test content 2");
            setLastModified(file2, 2025, 3, 10);

            // Create test file 3 in a subdirectory: Feb 20, 2025
            Path subDir = inputDir.resolve("sub");
            Files.createDirectories(subDir);
            Path file3 = subDir.resolve("test3.txt");
            Files.writeString(file3, "Test content 3");
            setLastModified(file3, 2025, 2, 20);

            // Run the main program
            FileSorter.main(new String[]{inputDir.toString(), outputDir.toString()});

            // Verify files were moved to correct locations
            assertFileExists(outputDir.resolve("2024").resolve("01").resolve("test1.txt"));
            assertFileExists(outputDir.resolve("2025").resolve("03").resolve("test2.txt"));
            assertFileExists(outputDir.resolve("2025").resolve("02").resolve("test3.txt"));

            // Verify input files were removed (moved)
            assertFileDoesNotExist(file1);
            assertFileDoesNotExist(file2);
            assertFileDoesNotExist(file3);

            System.out.println("All tests passed successfully!");

        } finally {
            // Clean up
            try (Stream<Path> walk = Files.walk(tempDir)) {
                walk.sorted(Comparator.reverseOrder())
                    .forEach(FileSorterTest::deleteFileSilently);
            }
        }
    }

    private static void setLastModified(Path file, int year, int month, int day) throws IOException {
        LocalDateTime ldt = LocalDateTime.of(year, month, day, 12, 0);
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Files.setLastModifiedTime(file, FileTime.from(instant));
    }

    private static void assertFileExists(Path file) {
        if (!Files.exists(file)) {
            throw new AssertionError("Test failed: Expected file does not exist at " + file);
        }
    }

    private static void assertFileDoesNotExist(Path file) {
        if (Files.exists(file)) {
            throw new AssertionError("Test failed: File should have been moved but still exists at " + file);
        }
    }

    private static void deleteFileSilently(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            // Ignore for cleanup
        }
    }
}
