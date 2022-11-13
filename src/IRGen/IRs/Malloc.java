package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Malloc extends IR{
    private Var arr;
    private int len;

    public Malloc(Var arr, int len) {
        this.arr = arr;
        this.len = len;
        super.setIrType(IRType.MALLOC);
    }

    public Var getDef() {
        return arr;
    }


    public Var getName() {
        return arr;
    }

    public void setName(Var name) {
        this.arr = name;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public String toString() {
        return "Malloc{" +
                "arr=" + arr +
                ", len=" + len +
                '}';
    }
}
