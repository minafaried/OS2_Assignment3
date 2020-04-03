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
        for(int i = 0; i < numberOfBlocks; i++) {
            bitMap.add(false);
        }
        
        blocks = new ArrayList<>(numberOfBlocks);
        for(int i = 0; i < numberOfBlocks; i++) {
            blocks.add(new MyBlock(i));
        }
        
        this.allocationAlgorithm = allocationAlgorithm;
        
        System.out.println(bitMap.size());
        System.out.println(blocks.size());
    }
    
    public boolean createFile(MyFile myFile, int fileSize) {
        if(allocationAlgorithm.equals(CONTIGUOUS)) {
            return createFileCont(myFile, fileSize);
        } else {
            return createFileIndexed(myFile, fileSize);
        }
    }
    
    public boolean createFolder(MyDirectory myDirectory) {
        if(allocationAlgorithm.equals(CONTIGUOUS)) {
            return createFolderCont(myDirectory);
        } else {
            return createFolderIndexed(myDirectory);
        }
    }
    
    public boolean deleteFile(MyFile myFile) {
        if(allocationAlgorithm.equals(CONTIGUOUS)) {
            return deleteFileCont(myFile);
        } else {
            return deleteFileIndexed(myFile);
        }
    }
    
    public boolean deleteFolder(MyDirectory myDirectory) {
        if(allocationAlgorithm.equals(CONTIGUOUS)) {
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
    
    private boolean createFileCont(MyFile myFile, int fileSize) {
        return true;
    }
    
    private boolean createFileIndexed(MyFile myFile, int fileSize) {
        return true;
    }
    
    private boolean createFolderCont(MyDirectory myDirectory) {
        return true;
    }
    
    private boolean createFolderIndexed(MyDirectory myDirectory) {
        return true;
    }
    
    private boolean deleteFileCont(MyFile myFile) {
        return true;
    }
    
    private boolean deleteFileIndexed(MyFile myFile) {
        return true;
    }
    
    private boolean deleteFolderCont(MyDirectory myDirectory) {
        return true;
    }
    
    private boolean deleteFolderIndexed(MyDirectory myDirectory) {
        return true;
    }
    
}
