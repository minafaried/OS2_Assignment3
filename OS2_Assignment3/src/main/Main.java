package main;

import data_structures.MyDirectory;
import data_structures.MyFile;
import virtual_file_system.FileStructure;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String FILE_PATH = "C:\\DiskStructure.vfs";

    public static void main(String[] args) {
        FileStructure fileStrucutre;
        boolean isFileExists = checkFile();

        if (isFileExists) {
            fileStrucutre = loadStructureFromFile();
        } else {
            fileStrucutre = initializeFile();
        }

        Parser parser = new Parser();
        while (true) {
            String command = inputString("Enter the command :", false);
            List<String> commandParts = parser.parseInput(command);
            if (commandParts == null) {
                println("Invalid Command.");
            } else { //The command is correct.
                String operation = commandParts.get(0);
                boolean result = true;

                if (operation.equals("CreateFile")) {
                    String filePath = commandParts.get(1);
                    int fileSize = Integer.parseInt(commandParts.get(2));
                    result = fileStrucutre.createFile(new MyFile(filePath), fileSize);
                } else if (operation.equals("CreateFolder")) {
                    String folderPath = commandParts.get(1);
                    result = fileStrucutre.createFolder(new MyDirectory(folderPath));
                } else if (operation.equals("DeleteFile")) {
                    String filePath = commandParts.get(1);
                    result = fileStrucutre.deleteFile(new MyFile(filePath));
                } else if (operation.equals("DeleteFolder")) {
                    String folderPath = commandParts.get(1);
                    result = fileStrucutre.deleteFolder(new MyDirectory(folderPath));
                } else if (operation.equals("DisplayDiskStatus")) {
                    fileStrucutre.displayDiskStatus();
                } else if (operation.equals("DisplayDiskStructure.")) {
                    fileStrucutre.displayFileStrucutre();
                } else {
                    break;
                }

                if (!result) {
                    println("Operation failed.");
                }
            }
        }
        saveDataToFile(fileStrucutre);
    }

    private static String inputString(String message, boolean isEmptyInputAllowed) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            print(message);
            if (isEmptyInputAllowed) {
                return scanner.nextLine();
            } else {
                String input = scanner.nextLine();
                if (input == null || input.equals("")) {
                    println("Input can not be empty.");
                } else {
                    return input;
                }
            }
        }
    }

    private static int inputInteger(String message, int lower, int upper) {
        Scanner scanner = new Scanner(System.in);
        String stringEntered;
        int integerEntered;
        while (true) {
            print(message);
            stringEntered = scanner.nextLine();
            try {
                integerEntered = Integer.parseInt(stringEntered);
            } catch (NumberFormatException e) {
                println("Not a number.");
                continue;
            }
            if (integerEntered >= lower && integerEntered <= upper) {
                return integerEntered;
            } else {
                println("Enter a number between " + lower + " & " + upper + " inclusively.");
            }
        }
    }

    private static void print(String message) {
        System.out.print(message);
    }

    private static void println(String message) {
        System.out.println(message);
    }

    private static void printAlgorithmsMenu() {
        println("1- " + FileStructure.CONTIGUOUS);
        println("2- " + FileStructure.INDEXED);
    }

    private static boolean checkFile() {
        File file = new File(FILE_PATH);
        return file.exists();
    }

    private static FileStructure loadStructureFromFile() {
        File file = new File(FILE_PATH);
        if (file.length() == 0) {
            file.delete();
            return initializeFile();
        } else {
            return loadDataFromFile();
        }
    }

    private static FileStructure initializeFile() {
        int numberOfBlocks = inputInteger("Enter the number of blocks :", 1, Integer.MAX_VALUE);
        String allocationAlgorithm;

        printAlgorithmsMenu();
        int integer = inputInteger("Enter a choice from the menu :", 1, 2);
        if (integer == 1) {
            allocationAlgorithm = FileStructure.CONTIGUOUS;
        } else {
            allocationAlgorithm = FileStructure.INDEXED;
        }
        FileStructure fileStructure = new FileStructure(numberOfBlocks, allocationAlgorithm);

        try {
            File file = new File(FILE_PATH);
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        saveDataToFile(fileStructure);
        return fileStructure;
    }

    private static FileStructure loadDataFromFile() {
        FileStructure fileStructure = null;
        try {
            File file = new File(FILE_PATH);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream object = new ObjectInputStream(fileInputStream);

            fileStructure = (FileStructure) object.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileStructure;
    }

    private static void saveDataToFile(FileStructure fileStructure) {
        try {
            File file = new File(FILE_PATH);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream object = new ObjectOutputStream(fileOutputStream);

            object.writeObject(fileStructure);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
