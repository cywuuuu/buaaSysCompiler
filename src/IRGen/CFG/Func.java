package IRGen.CFG;

import IRGen.IRs.IR;
import IRGen.IRs.Malloc;
import IRGen.IRs.Var;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Func {
    // contains several linked BBlock
    private static int idCnt = 0;
    private String id;
    private LinkedList<BBlock> bBlockLinkedList = new LinkedList<>();
    private BBlock nowBlock = null;
    private int funcParamCnt = 0;
    private int frameSize = 0;
    private HashMap<Var, Integer> varInReg = new HashMap<>(); // tmp used for reg
    private HashMap<Integer, Integer> regInStack = new HashMap<>();
    private HashMap<Var, Integer> varInStack = new HashMap<>();
    private HashMap<Var, Integer> arrInStack = new HashMap<>();

    public int getFrameSize() {
        return frameSize;
    }

    void addBBlock(BBlock bBlock) {

        bBlockLinkedList.addLast(bBlock);
    }

    boolean containBBlock(BBlock bBlock) {
        return bBlockLinkedList.contains(bBlock);
    }

    public String getId() {
        return id;
    }

    public BBlock getFirstBBlock() {
        return bBlockLinkedList.getFirst();
    }

    public Func(String id, int funcParamCnt) {
        this.id = id;
        this.funcParamCnt = funcParamCnt;
    }

    public void regAllocate() {
        for (BBlock bBlock : bBlockLinkedList) {
            Iterator<IR> item = bBlock.getIrsIterator();

            while (item.hasNext()) {
                IR ir = item.next();

                if (ir.getDef() != null) {
                    Var def = ir.getDef();
                    if (!varInReg.containsKey(def)) { //变量不在reg
                        if (ir instanceof Malloc) { // 函数内开辟数组，存在stack 里
                            arrInStack.put(def, frameSize);
                            frameSize += ((Malloc) ir).getLen() * 4;
                            def.setMemSize(((Malloc) ir).getLen() * 4);
                        } else {
                            varInStack.put(def, frameSize); // 普通变量
                            frameSize += 4;
                        }
                    }
                }

            }
        }

        for (Integer i : varInReg.values()) {

            regInStack.put(i, frameSize);
            if (!regInStack.containsKey(i)) {
                frameSize += 4;
            }

        }
        regInStack.put(31, frameSize);
        frameSize += 4;
    }

    /**
     * recover regs \\
     * pop stack  \\
     * jr ra \\
     * */
    public String funcRetAsm() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry: regInStack.entrySet()) {
            sb.append(String.format("lw $%d, %d($sp)\n", entry.getKey(), entry.getValue())); // recovering
        }
        sb.append(String.format("add $sp, $sp, %d\n", frameSize)); //pop frame
        sb.append("jr $ra\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: \n", id));
        for (BBlock f : bBlockLinkedList) {
            sb.append(f);
        }
        return sb.toString();
    }

    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        for (BBlock bBlock : bBlockLinkedList) {
            bBlock.varToMipsForm(varInReg, varInStack, arrInStack);
        }
        sb.append(String.format("\n%s: \n", id));
        frameSize += funcParamCnt * 4;
        sb.append(String.format("sub $sp, $sp, %d\n", frameSize));

        // func save
        for (Map.Entry<Integer, Integer> entry: regInStack.entrySet()) {
            sb.append(String.format("sw $%d, %d($sp)\n", entry.getKey(), entry.getValue())); // recovering
        }
        for (BBlock bBlock : bBlockLinkedList) {
            sb.append(bBlock.toAsm(this));
        }
        return sb.toString();
    }
}
