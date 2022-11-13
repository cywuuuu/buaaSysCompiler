package IRGen.IRs;

import IRGen.CFG.BBlock;

import java.util.HashSet;

public class JmpFalse extends IR {
    BBlock target;
    Var from;

    public JmpFalse(BBlock target, Var from) {
        this.target = target;
        this.from = from;
        super.setIrType(IRType.JMP_FALSE);
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
//        if (from == null) return a;
        if (from.getVarType() != VarType.NUMBER)
            a.add(from);
        return a;
    }
    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        Var fromC = from.deepCopy();
        if (from.getVarType() == VarType.SP) {


            sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), from.getSpForm()));
            fromC.setName(Var.tmpReg1());
        }
        else if (from.getVarType() == VarType.NUMBER) {
            if (Integer.parseInt(from.getName()) == 0) {
                return String.format("j %s\n", target.getLabel());
            }
            return "";
        }
        sb.append(String.format("beq $0, %s, %s\n", fromC.getName(), target.getLabel()));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "JmpFalse{" +
                "target=" + target.getLabel() +
                ", from=" + from +
                '}';
    }
}
