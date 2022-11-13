package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Not extends IR{
    private Var to;
    private Var from;

    public Not(Var to, Var from) {
        this.to = to;
        this.from = from;
        super.setIrType(IRType.NOT);
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
            sb.append(String.format("seq %s, %s, %s\n", Var.tmpReg1(), Var.zeroReg(), fromC.getName()));
            sb.append(String.format("sw %s, %s\n", Var.tmpReg1(), toC.getSpForm()));
        } else {
            sb.append(String.format("seq %s, %s, %s\n", toC.getName(), Var.zeroReg(), fromC.getName()));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Not{" +
                "to=" + to +
                ", from=" + from +
                '}';
    }
}
