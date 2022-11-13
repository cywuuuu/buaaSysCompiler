package IRGen.IRs;

import IRGen.CFG.BBlock;
import IRGen.CFG.Func;

import java.util.HashSet;

public class IR {
    private IRType irType;
    private BBlock owner;

    protected Func inFunc;

    public Func getInFunc() {
        return inFunc;
    }

    public void setInFunc(Func inFunc) {
        this.inFunc = inFunc;
    }


    public IRType getIrType() {

        return irType;
    }

    public void setIrType(IRType irType) {
        this.irType = irType;
    }

    public BBlock getOwner() {
        return owner;
    }

    public void setOwner(BBlock owner) {
        this.owner = owner;
    }

    public Var getDef() {
        return null;
    }

    public HashSet<Var> getUse() {
        return null;
    }

    public String toStr() {
        return "";
    }

    public String toAsm() {
        return "";
    }
}
