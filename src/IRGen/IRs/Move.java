package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Move extends IR{
    // move to, from
    // li a, 10
    private Var to;
    private Var from;

    public Move(Var to, Var from) {
        this.to = to;
        this.from = from;
        super.setIrType(IRType.MOVE);
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

    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        // move to: a, from: sp0 ->
        // lw $k1, spX  move a, $k1
        Var toC = to.deepCopy();
        Var fromC = from.deepCopy();
        if (fromC.getVarType() == VarType.SP) {
            // move to: a, from: sp0 ->
            // lw $k1, spX  move a, $k1
            sb.append(String.format("lw %s, %d($sp)\n", Var.tmpReg1(), fromC.getOffset()));
            fromC.setName(Var.tmpReg1());
        }

        if (toC.getVarType() == VarType.REG && fromC.getVarType() == VarType.NUMBER) {
            sb.append(String.format("li %s, %s\n", toC.getName(), fromC.getName()));
        } else if (toC.getVarType() == VarType.REG && fromC.getVarType() == VarType.REG) {
            sb.append(String.format("move %s, %s\n", toC.getName(), fromC.getName()));
        } else if (toC.getVarType() == VarType.SP && fromC.getVarType() == VarType.NUMBER) {
            sb.append(String.format("li %s, %s\n", Var.tmpReg1(), fromC.getName()));
            fromC.setName(Var.tmpReg1());
            sb.append(String.format("sw %s, %d($sp)\n", fromC.getName(), toC.getOffset()));
        } else { //sp, reg
            sb.append(String.format("sw %s, %d($sp)\n", fromC.getName(), toC.getOffset()));
        }

        return sb.toString();

    }

    @Override
    public String toString() {
        return "Move{" +
                "to=" + to +
                ", from=" + from +
                '}';
    }
}
