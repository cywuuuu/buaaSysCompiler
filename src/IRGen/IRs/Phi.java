package IRGen.IRs;

import IRGen.CFG.BBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Phi extends IR{
    private HashMap<String, BBlock> fromBlocks = new HashMap<>();
    private Var to;
    private Var from;

    public Var getDef() {
        return to;
    }

    public HashSet<String> getUseBlock() {
        return (HashSet<String>) fromBlocks.keySet();
    }

    public Phi(HashMap<String, BBlock> fromBlocks, Var to, Var from) {
        this.fromBlocks = fromBlocks;
        this.to = to;
        this.from = from;
        super.setIrType(IRType.PHI);
    }


}
