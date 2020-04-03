package data_structures;

public class MyFile {

    private String path;
    private int allocatedBlock;
    //The allocatedBlock refers to the start in case of Cont,
    //and refers to index block in case of Indexed.

    public MyFile(String path, int allocatedBlock) {
        this.path = path;
        this.allocatedBlock = allocatedBlock;
    }

    public MyFile(String path) {
        this(path, -1);
    }

    public MyFile() {
        this(null, -1);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getAllocatedBlock() {
        return allocatedBlock;
    }

    public void setAllocatedBlock(int allocatedBlock) {
        this.allocatedBlock = allocatedBlock;
    }

    @Override
    public String toString() {
        return "MyFile{" + "path=" + path + ", allocatedBlock=" + allocatedBlock + '}';
    }
}
