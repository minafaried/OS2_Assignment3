package data_structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBlock implements Serializable {

    private int index;
    //The index of the current (self) block.
    private List<Integer> data;
    //data(0) holds the length in case of Cont.
    //data holds an array of pointers to data blocks in case of Indexed.

    public MyBlock(int index, List<Integer> data) {
        this.index = index;
        this.data = data;
    }
    
    public MyBlock(int index) {
        this(index, new ArrayList<>());
    }

    public MyBlock() {
        this(-1, new ArrayList<>());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyBlock{" + "index=" + index + ", data=" + data + '}';
    }
}
