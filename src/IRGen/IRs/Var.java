package IRGen.IRs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Var  implements Serializable {
    private String name;

    private static int IRStrCnt = 0;
    private static int IRVarCnt = 0;
    private static int IrTmpCnt = 0;


    public static void clearCnt() {
        IRStrCnt = 0;
        IRVarCnt = 0;
        IrTmpCnt = 0;
    }

    public static Var makeIrStrId() {
        return new Var(String.format("str_%d", ++IRStrCnt));
    }

    public static Var makeIrVarId(String name) {
        return new Var(String.format("var_%s_%d", name, ++IRVarCnt));
    }
    public static Var getIrVarWithId(String name, int id) {
        return new Var(String.format("var_%s_%d", name, id));
    }
    public static Var makeIrTmpId() {
        return new Var(String.format("tmp_%d", ++IrTmpCnt));
    }

    public static String makeIrFuncId(String name) { return String.format("_%s",name); }

    private int memSize = 0;

    public int getMemSize() {
        return memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    public Var(String name) {
        this.name = name;
    }

    public static String tmpReg1() {
        return "$k0";
    }

    public static String tmpReg2() {
        return "$k1";
    }

    public static String tmpReg3() {
        return "$gp";
    }

    public static String ioReg() {
        return "$v0";
    }

    public static String spReg() {
        return "$sp";
    }

    public static String zeroReg() {
        return "$0";
    }

    public static String paramReg(int i) {return String.format("$a%d", i); }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getOffset() {
        return Integer.parseInt(name.substring(2));
    }

    @Override
    public String toString() {
        return name;
    }

    public VarType getVarType() {
        if (name.startsWith("sp")) {
            return VarType.SP;
        } else if (name.startsWith("array")) {
            return VarType.ARRAY;
        } else if (name.startsWith("$")) {
            return VarType.REG;
        } else if (name.matches("[-+]*\\d+")) {
            return VarType.NUMBER;
        } else if (name.startsWith("void")) {
            return VarType.VOIDRET;
        } else {
            return VarType.GLOBVAR;
        }
    }

    public String getSpForm() {
        return String.format("%s($sp)", name.substring(2));
    }

    public String getTmpArrayBase() { return name.substring(5); }

    public Var deepCopy()  {
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Var desList = null;
        try {
            desList = (Var) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return desList;
    }

}

