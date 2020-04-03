package virtual_file_system;

import java.io.Serializable;

public class Disk implements Serializable {

    private final int numberOfBlocks;
    private final int totalSpace;
    private int allocatedSpace;
    private int allocatedBlocks;
    private final int SIZE_OF_BLOCK = 1;

    public Disk(int numberOfBlocks) {
        this.numberOfBlocks = numberOfBlocks;
        this.totalSpace = numberOfBlocks * SIZE_OF_BLOCK;
        this.allocatedSpace = 0;
        this.allocatedBlocks = 0;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public int getTotalSpace() {
        return totalSpace;
    }

    public int getAllocatedSpace() {
        return allocatedSpace;
    }

    public int getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public int getEmptySpace() {
        return totalSpace - allocatedSpace;
    }

    public int getEmptyBlocks() {
        return numberOfBlocks - allocatedBlocks;
    }

    public int getBlockSize() {
        return SIZE_OF_BLOCK;
    }

    public void addToAllocatedSpace(int space) {
        allocatedSpace += space;
    }

    public void addToAllocatedBlocks(int blocks) {
        allocatedBlocks += blocks;
    }
    
    public void displayDisk() {
        
    }
}
