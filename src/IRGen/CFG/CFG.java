package IRGen.CFG;

import IRGen.IRs.IR;
import SymTab.SymTab;

import java.util.HashMap;
import java.util.HashSet;

public class CFG {

    private static CFG cfg = null;

    public static synchronized CFG getInstance() {
        if (cfg == null) {
            cfg = new CFG();
        }
        return cfg;
    }

    private HashMap<String, Func> funcs = new HashMap<>();
    private Func nowFunc = null;
    private BBlock nowBBlock = null;
    private int BBCnt = 0;
    public void insertFunc(Func func) {
        nowFunc = func;
        funcs.put(func.getId(), func);
    }

    public boolean setNowFunc(Func func) {
        if (funcs.containsKey(func.getId())) {
            nowFunc = func;
            return true;
        } else {
            insertFunc(func);
            nowFunc = func;
            return false;
        }
    }

    public BBlock getNowBBlock() {
        return nowBBlock;
    }

    public void insertBBlock(BBlock bBlock) {
        if (bBlock.getId().equals(nowBBlock.getId())) {
            return;
        }
        nowBBlock = bBlock;
        if (!nowFunc.containBBlock(bBlock)) {
            nowFunc.addBBlock(bBlock);
        }

    }


    public void setNowBBlock(BBlock bBlock) {

        if (nowBBlock != null && bBlock.getId().equals(nowBBlock.getId())) {
            return;
        }
        nowBBlock = bBlock;
        if (!nowFunc.containBBlock(bBlock)) {
            nowFunc.addBBlock(bBlock);

        }

    }

    public void insertIrToNowBBlock(IR ir) {
        nowBBlock.addIrLast(ir);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Func f : funcs.values()) {
            sb.append(f);
        }
        return sb.toString();
    }

    public void regAllocate() {
        for (Func func:funcs.values()) {
            func.regAllocate();
        }
    }

    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        sb.append(".text\n");
        sb.append("jal _main\n");
        sb.append("ori $v0, $0, 10\n" +
                "syscall\n");
        for (Func f : funcs.values()) {
            sb.append(f.toAsm());
        }

        return sb.toString();
    }




}
