# Dis-o-Matic: Disk Scheduling Algorithm

_This project is developed by **Jake Mondragon**, **Benedict Pagba**, **Justin Magne Agaton**, and **Niel Frias**_

## Overview

This application is an implementation in Java of 6 Disk Scheduling Algorithms: FCFS, SSTF, SCAN, C-SCAN, LOOK, C-LOOK.
## Appendix A

- The cylinder line is always starting with 0 and ending with 199.
- Order of Requests Queue: (maximum requests is up to 40).
- R/W head starting position.
- User has 3 choices how to generate the data needed by the simulator (See Appendix B)

## Appendix B

Generated data can be obtained through random, user-defined input, and user-defined input from a file

- Random: the program will randomly generate Order of Request Queue.
- User-defined input: the user will input the rOrder of Request Queuee through an input screen in the simulator.
- User-defined input from a CSV file: the user will input the data by reading a CSV file.

---

## Installation & Setup Guide

### 1. Clone the Repository

```bash
git clone <your-repo-url>
```

### 2. Open the Project in VS Code

- Make sure you have the **Java Extension Pack** installed and use Java 21.
- Open the `Main.java` file.

To compile and run the project via terminal:

```bash
find . -name "*.class" -type f -delete && javac --release 21 -d out $(find . -name "*.java") && java -cp "out;." Main
```

### 3. Run the Application

You can run the simulator by:

- Clicking the Run Java button in the top-right of VS Code

### 4. Create and Run a jar file

```bash
jar cfe Disc-o-Matic.jar Main -C out .
java -jar Disc-o-Matic.jar
```

### 5. Run the .exe by double clicking the file
