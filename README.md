# FileSorter

A Java utility program that recursively sorts files from an input directory into an output directory based on their last modified date. Files are organized into `YYYY/MM` folders within the destination path.

## Features

- Scans an input directory and subdirectories for files.
- Extracts the "last modified" timestamp of each file.
- Moves the file to `[output_dir]/YYYY/MM/filename` based on the file timestamp.
- Safely skips moving a file if another file with the same name already exists in the target structure, writing an explanation to the terminal.

## Prerequisites

- Java Development Kit (JDK) 8 or later must be installed on your machine.

---

## Option 1: Build and Run (Standard)

If you are inside the `file sorter` directory containing the `src` folder, you can compile the program using the `javac` command:

```powershell
javac -d . src\com\donkersloot\utils\FileSorter.java
```

This compiles the source code and produces the necessary `.class` files in the correct directory format: `.\com\donkersloot\utils\FileSorter.class`.

After building, run the program via the command line by passing in the input directory and output directory as arguments:

```powershell
java com.donkersloot.utils.FileSorter <path_to_input_dir> <path_to_output_dir>
```

---

## Option 2: Build and Run (Executable JAR)

Alternatively, you can package the program into a standalone executable JAR file.

**1. Compile the Java source file:**
   ```powershell
   javac -d . src\com\donkersloot\utils\FileSorter.java
   ```

**2. Package the compiled classes into an executable JAR file**, specifying the main class:
   ```powershell
   jar cfe FileSorter.jar com.donkersloot.utils.FileSorter com\donkersloot\utils\FileSorter.class
   ```

This will create a `FileSorter.jar` file in your current directory.

After building the JAR file, run the program via the command line:

```powershell
java -jar FileSorter.jar <path_to_input_dir> <path_to_output_dir>
```

---

### Example Usage

Assuming you have `input` and `output` folders in the same directory:

```powershell
java -jar FileSorter.jar input output
```

*(Or simply `java com.donkersloot.utils.FileSorter input output` if not using the JAR)*

Output directory structure will look like: 
```text
output/
  ├── 2026/
  │   ├── 01/
  │   │   └── my_document.txt
  │   └── 02/
  │       ├── photo1.jpg
  │       └── photo2.png
  └── 2025/
      └── 12/
          └── old_notes.md
```
