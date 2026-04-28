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

To compile and run the project from **Windows Command Prompt**:

```cmd
cd /d "c:\Users\Geralyn\OneDrive\Desktop\CMSC 125\Disk-o-Matic" && if exist out rmdir /s /q out && mkdir out && javac --release 21 -d out Main.java DiscOMaticSimulator.java algorithms\*.java gui\*.java && java -cp "out;." Main
```

To compile and run the project from **PowerShell**:

```powershell
Set-Location "c:\Users\Geralyn\OneDrive\Desktop\CMSC 125\Disk-o-Matic"
if (Test-Path out) { Remove-Item out -Recurse -Force }
New-Item -ItemType Directory -Path out | Out-Null
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --release 21 -d out $files
java -cp "out;." Main
```

### 3. Run the Application

You can run the simulator by:

- Clicking the Run Java button in the top-right of VS Code
- Running either terminal command above

### 4. Create and Run a jar file

Build the JAR from **Command Prompt**:

```cmd
cd /d "c:\Users\Geralyn\OneDrive\Desktop\CMSC 125\Disk-o-Matic" && if exist out rmdir /s /q out && mkdir out && javac --release 21 -d out Main.java DiscOMaticSimulator.java algorithms\*.java gui\*.java && xcopy /e /i /y img out\img >nul && jar --create --file DiskOMatic.jar --main-class Main -C out .
```

Run the JAR:

```cmd
java -jar DiskOMatic.jar
```

Keep the `dataset` folder beside the JAR if you want the Import button to open the sample datasets correctly.

### 5. Run the .exe by double clicking the file
