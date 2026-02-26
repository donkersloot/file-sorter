package com.donkersloot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class FileSorter {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java com.donkersloot.utils.FileSorter <input_directory> <output_directory>");
            System.exit(1);
        }

        Path inputDir = Paths.get(args[0]);
        Path outputDir = Paths.get(args[1]);

        if (!Files.exists(inputDir) || !Files.isDirectory(inputDir)) {
            System.err.println("Error: Input directory does not exist or is not a directory.");
            System.exit(1);
        }

        try {
            processDirectory(inputDir, outputDir);
            System.out.println("File sorting complete.");
        } catch (IOException e) {
            System.err.println("Error processing directories: " + e.getMessage());
        }
    }

    private static void processDirectory(Path inputDir, Path outputDir) throws IOException {
        try (Stream<Path> paths = Files.walk(inputDir)) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    moveFile(file, outputDir);
                } catch (IOException e) {
                    System.err.println("Failed to move file " + file.toString() + ": " + e.getMessage());
                }
            });
        }
    }

    private static void moveFile(Path file, Path outputDir) throws IOException {
        // Get last modified time
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
        Instant lastModifiedTime = attrs.lastModifiedTime().toInstant();
        ZonedDateTime zdt = lastModifiedTime.atZone(ZoneId.systemDefault());

        // Format YYYY/MM
        String year = String.format("%04d", zdt.getYear());
        String month = String.format("%02d", zdt.getMonthValue());

        // Create target directory structure
        Path targetDir = outputDir.resolve(year).resolve(month);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Path targetFile = targetDir.resolve(file.getFileName());

        // Check if file already exists in the output directory
        if (Files.exists(targetFile)) {
            System.out.println("Skipped (already exists): " + targetFile.toString());
            return;
        }

        // Move the file
        Files.move(file, targetFile);
        System.out.println("Moved: " + file.toString() + " -> " + targetFile.toString());
    }
}
