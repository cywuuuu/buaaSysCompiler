package IRGen.CFG;

import IRGen.IRs.IR;
import IRGen.IRs.Var;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class BBlock implements Serializable {
    private static int idCnt = 0;
    private String id;
    private String hintName = "";

    public void setHintName(String hintName) {
        this.hintName = hintName;
    }

    private LinkedList<IR> irs = new LinkedList<>();
    private ArrayList<BBlock> preds = new ArrayList<>();
    private ArrayList<BBlock> succs = new ArrayList<>();
    private ArrayList<String> varDef = new ArrayList<>();

    public BBlock() {
        this.id = String.valueOf(idCnt++);
    }

    public BBlock(String hintName) {
        this.id = String.valueOf(idCnt++);
        this.hintName = hintName;
    }

    public Iterator<IR> getIrsIterator() {
        return irs.iterator();
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return "label" + this.id + "_" + this.hintName;
    }

    public void addIrLast(IR ir) {
        ir.setOwner(this);
        irs.addLast(ir);
    }

    public void addIrFirst(IR ir) {
        ir.setOwner(this);
        irs.addFirst(ir);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("block_%s:\n", id));
        for (IR ir : irs) {
            sb.append(String.format("%s\n", ir));
        }
        return sb.toString();
    }

    public void removeIr(IR ir) {
        irs.remove(ir);
    }

    public String toAsm(Func infunc) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLabel());
        sb.append(":\n");
//        sb.append("#").append(this.hintName).append("\n");
        for (IR ir : irs) {
            ir.setInFunc(infunc);

            sb.append("\n#").append(ir).append("\n");

            sb.append(ir.toAsm());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void varToMipsForm(HashMap<Var, Integer> varInReg,
                              HashMap<Var, Integer> varInStack, HashMap<Var, Integer> arrInStack) {
        for (IR ir : irs) {
            Var def = ir.getDef();
            if (def != null) {
                if (varInReg.containsKey(def)) {
                    String name = def.getName();
                    def.setName(String.format("$%d", varInReg.get(def)));
                } else if (varInStack.containsKey(def)) {
                    def.setName(String.format("sp%d", varInStack.get(def)));
                } else if (arrInStack.containsKey(def)) {
                    def.setName(String.format("array%d", arrInStack.get(def)));
                }
            }
            HashSet<Var> uses = ir.getUse();
            if (uses != null) {
                for (Var use : uses) {
                    if (varInReg.containsKey(use)) {

                        use.setName(String.format("$%d", varInReg.get(use)));
                    } else if (varInStack.containsKey(use)) {
                        use.setName(String.format("sp%d", varInStack.get(use)));
                    } else if (arrInStack.containsKey(def)) {
                        use.setName(String.format("array%d", arrInStack.get(use)));
                    }
                }
            }

        }

    }

    public void regAllocate() {

    }

    public BBlock deepCopy() {
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
        BBlock desList = null;
        try {
            desList = (BBlock) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return desList;
    }
}
