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

    private boolean createFileCont(MyFile myFile, int fileSize) {
        return true;
    }

    private boolean createFileIndexed(MyFile myFile, int fileSize) {
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
            for (int i = 0; i < pointer.getDirectories().size(); i++) {
                if(path.equals( pointer.getDirectories().get(i).getPath()))
                {
                    return false;
                }
            }
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
        return true;
    }

    private boolean deleteFileIndexed(MyFile myFile) {
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
