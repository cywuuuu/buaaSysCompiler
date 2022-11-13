package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Neg extends IR{
    private Var to;
    private Var from;

    public Neg(Var to, Var from) {
        this.to = to;
        this.from = from;
        super.setIrType(IRType.NEG);
    }


    public Var getDef() {
        return to;
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (from.getVarType() != VarType.NUMBER)
            a.add(from);
        return a;
    }


    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        Var toC = to.deepCopy();
        Var fromC = from.deepCopy();
        if (fromC.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), fromC.getSpForm()));
            fromC.setName(Var.tmpReg1());
        }

        if (toC.getVarType() == VarType.SP) {
            sb.append(String.format("neg %s, %s\n", Var.tmpReg1(), fromC.getName()));//待check
            sb.append(String.format("sw %s, %s\n", Var.tmpReg1(), toC.getSpForm()));
        } else {
            sb.append(String.format("neg %s, %s\n", toC.getName(), fromC.getName()));//待check
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Neg{" +
                "to=" + to +
                ", from=" + from +
                '}';
    }
}
