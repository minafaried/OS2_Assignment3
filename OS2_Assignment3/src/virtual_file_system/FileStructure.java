package virtual_file_system;

import data_structures.MyBlock;
import data_structures.MyDirectory;
import data_structures.MyFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileStructure implements Serializable {

    private final Disk disk;
    private final MyDirectory root;
    private final List<Boolean> bitMap;
    private final List<MyBlock> blocks;
    private final String allocationAlgorithm;

    public static final String CONTIGUOUS = "Contiguous Allocation.";
    public static final String INDEXED = "Indexed Allocation.";

    public FileStructure(int numberOfBlocks, String allocationAlgorithm) {
        disk = new Disk(numberOfBlocks);
        root = new MyDirectory("root");

        bitMap = new ArrayList<>(numberOfBlocks);
        for (int i = 0; i < numberOfBlocks; i++) {
            bitMap.add(false);
        }

        blocks = new ArrayList<>(numberOfBlocks);
        for (int i = 0; i < numberOfBlocks; i++) {
            blocks.add(new MyBlock(i));
        }

        this.allocationAlgorithm = allocationAlgorithm;

        // System.out.println(bitMap.size());
        // System.out.println(blocks.size());
    }

    public boolean createFile(MyFile myFile, int fileSize) {
        if (allocationAlgorithm.equals(CONTIGUOUS)) {
            return createFileCont(myFile, fileSize);
        } else {
            return createFileIndexed(myFile, fileSize);
        }
    }

    public boolean createFolder(MyDirectory myDirectory) {
        if (allocationAlgorithm.equals(CONTIGUOUS)) {
            return createFolderCont(myDirectory);
        } else {
            return createFolderIndexed(myDirectory);
        }
    }

    public boolean deleteFile(MyFile myFile) {
        if (allocationAlgorithm.equals(CONTIGUOUS)) {
            return deleteFileCont(myFile);
        } else {
            return deleteFileIndexed(myFile);
        }
    }

    public boolean deleteFolder(MyDirectory myDirectory) {
        if (allocationAlgorithm.equals(CONTIGUOUS)) {
            return deleteFolderCont(myDirectory);
        } else {
            return deleteFolderIndexed(myDirectory);
        }
    }

    public void displayFileStrucutre() {

    }

    public void displayDiskStatus() {
        disk.displayDisk();
    }

    public boolean checkFileExistence(MyDirectory pointer, MyFile myFile) {
        for (int i = 0; i < pointer.getFiles().size(); i++) {
            if (pointer.getFiles().get(i).getPath().equals(myFile.getPath())) {
                return false;
            }
        }
        return true;
    }

    private boolean createFileCont(MyFile myFile, int fileSize) {
        /*
         Pre-requests:
         1-	The path is already exist
         2-	No file with the same name is already created under this path
         3-	Enough space exists
         */

        // Check if there is an enough space for the file
        boolean foundStart = false, enoughSpace = false;
        int startIndex = -1, foundSize = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (foundStart == false && bitMap.get(i).equals(false)) {
                foundStart = true;
                startIndex = i;
                foundSize += 1;
            } else if (bitMap.get(i).equals(false)) {
                foundSize += 1;
            } else {
                startIndex = -1;
                foundSize = 0;
            }
            if (foundSize == fileSize) {
                enoughSpace = true;
                break;
            }
        }

        // Check if the path exists and if the file exists
        String[] pathArray = myFile.getPath().split("/");
        String lastfolder = "root";
        boolean pathNotExists = false, fileNotExists = true;
        for (int i = 1; i < pathArray.length - 1; i++) {
            lastfolder += "/" + pathArray[i];
        }
        MyDirectory pointer = finedDirectory(root, lastfolder);
        if (pointer != null) {
            pathNotExists = true;
            fileNotExists = checkFileExistence(pointer, myFile);
        }

        // if the Pre-requests are true
        if (enoughSpace && pathNotExists && fileNotExists) {
            myFile.setAllocatedBlock(startIndex);
            pointer.addFile(myFile);
            List<Integer> Data = null;
            Data.add(fileSize);
            bitMap.set(startIndex, true);
            blocks.get(startIndex).setData(Data);
            blocks.get(startIndex).setIndex(startIndex);
            for (int i = startIndex+1; i < fileSize+startIndex; i++) {
                bitMap.set(i, true);
                blocks.get(i).setData(null);
                blocks.get(i).setIndex(i);
            }
            disk.addToAllocatedBlocks(fileSize);
            disk.addToAllocatedSpace(fileSize);
        } else {
            return false;
        }

        return true;
    }

    private boolean createFileIndexed(MyFile myFile, int fileSize) {
        /*
         Pre-requests:
         1-	The path is already exist
         2-	No file with the same name is already created under this path
         3-	Enough space exists
         */

        // Check if there is an enough space for the file
        boolean enoughSpace = false;
        int startIndex = -1, foundSize = 0;
        List<Integer> Indexes = null;
        for (int i = 0; i < blocks.size(); i++) {
            if (startIndex == -1 && bitMap.get(i).equals(false)) {
                startIndex = i;
                foundSize += 1;
            } else if (bitMap.get(i).equals(false)) {
                foundSize += 1;
                Indexes.add(i);
            }
            if (foundSize == fileSize) {
                enoughSpace = true;
            }
        }

        // Check if the path exists and if the file exists
        String[] pathArray = myFile.getPath().split("/");
        String lastfolder = "root";
        boolean pathNotExists = false, fileNotExists = true;
        for (int i = 1; i < pathArray.length - 1; i++) {
            lastfolder += "/" + pathArray[i];
        }
        MyDirectory pointer = finedDirectory(root, lastfolder);
        if (pointer != null) {
            pathNotExists = true;
            fileNotExists = checkFileExistence(pointer, myFile);
        }
        
        if (enoughSpace && pathNotExists && fileNotExists){
            pointer.addFile(myFile);
            myFile.setAllocatedBlock(startIndex);
            bitMap.set(startIndex, true);
            blocks.get(startIndex).setData(Indexes);
            blocks.get(startIndex).setIndex(startIndex);
            
            for (int i = 0; i < Indexes.size(); i++) {
                bitMap.set(Indexes.get(i), true);
                blocks.get(Indexes.get(i)).setData(null);
                blocks.get(Indexes.get(i)).setIndex(Indexes.get(i));
            }
            disk.addToAllocatedBlocks(fileSize);
            disk.addToAllocatedSpace(fileSize);
        }
        else{
            return false;
        }

        return true;
    }

    private MyDirectory finedDirectory(MyDirectory root, String lastfolder) {
        if (root.getPath().equals(lastfolder)) {
            return root;
        }
        if (root.getDirectories().isEmpty()) {
            return null;
        }

        for (int i = 0; i < root.getDirectories().size(); i++) {
            return finedDirectory(root.getDirectories().get(i), lastfolder);

        }
        return null;
    }

    private boolean createFolderCont(MyDirectory myDirectory) {
        // root/folder1/folder2
        String path = myDirectory.getPath();
        String patharr[] = path.split("/");
        String lastfolder = "root";
        for (int i = 1; i < patharr.length - 1; i++) {
            lastfolder += "/" + patharr[i];
        }
        //System.out.println(lastfolder);
        MyDirectory pointer = finedDirectory(root, lastfolder);
        //pointer.display();
        if (pointer != null) {
            pointer.addDirectory(myDirectory);
            // pointer.display();
            return true;
        }

        return false;
    }

    private boolean createFolderIndexed(MyDirectory myDirectory) {

        return createFolderCont(myDirectory);
    }

    private boolean deleteFileCont(MyFile myFile) {        
        // Check if the path exists and if the file exists
        String[] pathArray = myFile.getPath().split("/");
        String lastfolder = "root";
        boolean pathNotExists = false, fileNotExists = true;
        for (int i = 1; i < pathArray.length - 1; i++) {
            lastfolder += "/" + pathArray[i];
        }
        MyDirectory pointer = finedDirectory(root, lastfolder);
        if (pointer != null) {
            pathNotExists = true;
            fileNotExists = checkFileExistence(pointer, myFile);
        }
        
        int index = -1;
        int startIndex = -1 , fileSize = 0;
        List <Integer> data = null;
        if (pathNotExists && !fileNotExists){
            for (int i = 0; i < pointer.getFiles().size(); i++) {
                if  (pointer.getFiles().get(i).getPath().equals(myFile.getPath())) {
                    index = i;
                    break;
                }   
            }
            startIndex = pointer.getFiles().get(index).getAllocatedBlock();
            data = blocks.get(index).getData();
            fileSize = data.get(0);
            for (int i = startIndex; i < fileSize+startIndex; i++) {
                bitMap.set(i, false);
            }
            pointer.removeFile(index);
            int subtractSize = -(fileSize);
            disk.addToAllocatedBlocks(subtractSize);
            disk.addToAllocatedSpace(subtractSize);
        }
        else{
            return false;
        }
        return true;
    }

    private boolean deleteFileIndexed(MyFile myFile) {
        
        // Check if the path exists and if the file exists
        String[] pathArray = myFile.getPath().split("/");
        String lastfolder = "root";
        boolean pathNotExists = false, fileNotExists = true;
        for (int i = 1; i < pathArray.length - 1; i++) {
            lastfolder += "/" + pathArray[i];
        }
        MyDirectory pointer = finedDirectory(root, lastfolder);
        if (pointer != null) {
            pathNotExists = true;
            fileNotExists = checkFileExistence(pointer, myFile);
        }
        
        int index = -1;
        int startIndex = -1;
        List <Integer> data = null;
        
        if (pathNotExists && !fileNotExists){
            for (int i = 0; i < pointer.getFiles().size(); i++) {
                if  (pointer.getFiles().get(i).getPath().equals(myFile.getPath())) {
                    index = i;
                    break;
                }   
            }
            startIndex = pointer.getFiles().get(index).getAllocatedBlock();
            data = blocks.get(index).getData();
            for (int i = 0; i < data.size(); i++) {
                bitMap.set(data.get(i), false);
            }
            pointer.removeFile(index);
            int subtractSize = -(data.size()+1);
            disk.addToAllocatedBlocks(subtractSize);
            disk.addToAllocatedSpace(subtractSize);
        }
        else{
            return false;
        }
        return true;
    }

    private void deletefilescont(MyDirectory root, MyDirectory deletedfile, String type) {

        if (deletedfile.getDirectories().isEmpty()) {
            for (int i = 0; i < deletedfile.getFiles().size(); i++) {
                if (type.equals(FileStructure.CONTIGUOUS)) {
                    deleteFileCont(deletedfile.getFiles().get(i));
                } else {
                    deleteFileIndexed(deletedfile.getFiles().get(i));
                }

            }
            root.getDirectories().remove(deletedfile);
            return;
        }

        for (int i = 0; i < deletedfile.getDirectories().size(); i++) {
            deletefilescont(deletedfile, deletedfile.getDirectories().get(i), type);

        }
    }

    private boolean deleteFolderCont(MyDirectory myDirectory) {
        String path = myDirectory.getPath();
        String patharr[] = path.split("/");
        String lastfolder = "root";
        for (int i = 1; i < patharr.length - 1; i++) {
            lastfolder += "/" + patharr[i];
        }
        MyDirectory lastfolderpointer = finedDirectory(root, lastfolder);
        MyDirectory folderpointer = null;
        for (int i = 0; i < lastfolderpointer.getDirectories().size(); i++) {
            if (path.equals(lastfolderpointer.getDirectories().get(i).getPath())) {
                folderpointer = lastfolderpointer.getDirectories().get(i);
            }
        }
        lastfolderpointer.display();
        folderpointer.display();
        deletefilescont(lastfolderpointer, folderpointer, FileStructure.CONTIGUOUS);
        // pointer.display();
        return true;
    }

    private boolean deleteFolderIndexed(MyDirectory myDirectory) {
        String path = myDirectory.getPath();
        String patharr[] = path.split("/");
        String lastfolder = "root";
        for (int i = 1; i < patharr.length - 1; i++) {
            lastfolder += "/" + patharr[i];
        }
        MyDirectory lastfolderpointer = finedDirectory(root, lastfolder);
        MyDirectory folderpointer = null;
        for (int i = 0; i < lastfolderpointer.getDirectories().size(); i++) {
            if (path.equals(lastfolderpointer.getDirectories().get(i).getPath())) {
                folderpointer = lastfolderpointer.getDirectories().get(i);
            }
        }
        lastfolderpointer.display();
        folderpointer.display();
        deletefilescont(lastfolderpointer, folderpointer, FileStructure.INDEXED);
        return true;
    }

}
