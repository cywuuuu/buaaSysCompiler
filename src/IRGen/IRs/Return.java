package IRGen.IRs;

import IRGen.CFG.Func;

import java.util.HashSet;

public class Return extends IR {
    private Var retVal;

    public Return(Var retVal) {
        this.retVal = retVal;
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (retVal.getVarType() != VarType.NUMBER)
            a.add(retVal);
        return a;
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        if (retVal.getVarType() == VarType.VOIDRET) {
            ;
        } else if (retVal.getVarType() == VarType.REG) {
            sb.append(String.format("move %s, %s\n", Var.ioReg(), retVal.getName()));
        } else if (retVal.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.ioReg(), retVal.getSpForm()));
        } else if (retVal.getVarType() == VarType.NUMBER) {
            sb.append(String.format("li %s, %s\n", Var.ioReg(), retVal.getName()));
        }
        sb.append(inFunc.funcRetAsm());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Return{" +
                "retVal=" + retVal +
                '}';
    }
}
