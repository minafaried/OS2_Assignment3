package data_structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyDirectory implements Serializable {
    
    private String path;
    private List<MyFile> files;
    private List<MyDirectory> directories;

    public MyDirectory(String path, List<MyFile> files, List<MyDirectory> directories) {
        this.path = path;
        this.files = files;
        this.directories = directories;
    }

    public MyDirectory(String path) {
        this(path, new ArrayList<>(), new ArrayList<>());
    }

    public MyDirectory() {
        this(null, new ArrayList<>(), new ArrayList<>());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MyFile> getFiles() {
        return files;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    public List<MyDirectory> getDirectories() {
        return directories;
    }

    public void setDirectories(List<MyDirectory> directories) {
        this.directories = directories;
    }

    public void addFile(MyFile myFile) {
        files.add(myFile);
    }

    public void addDirectory(MyDirectory myDirectory) {
        directories.add(myDirectory);
    }
    public void display()
    {
        System.out.println("path :"+path);
        System.out.println("Directorys :"+directories);
        System.out.println("files :"+files); 
    }
    
}
