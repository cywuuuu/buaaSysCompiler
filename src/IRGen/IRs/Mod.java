package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Mod extends IR{
    private Var to;
    private Var from1;
    private Var from2;


    public Mod(Var to, Var from1, Var from2) {
        this.to = to;
        this.from1 = from1;
        this.from2 = from2;
        super.setIrType(IRType.MOD);
    }

    public Var getDef() {
        return to;
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (from1.getVarType() != VarType.NUMBER)
            a.add(from1);
        if (from2.getVarType() != VarType.NUMBER)
            a.add(from2);
        return a;
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        Var toC = to.deepCopy();
        Var from1C = from1.deepCopy();
        Var from2C = from2.deepCopy();
        if (from1C.getVarType() == VarType.NUMBER) {
            sb.append(String.format("li %s, %s\n", Var.tmpReg1(), from1C.getName()));
            from1C.setName(Var.tmpReg1());
        } else if (from1C.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), from1C.getSpForm()));
            from1C.setName(Var.tmpReg1());
        }

        if (from2C.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.tmpReg2(), from2C.getSpForm()));
            from2C.setName(Var.tmpReg2());
        } else if (from2C.getVarType() == VarType.NUMBER) {
            //待优化
            sb.append(String.format("li %s, %s\n", Var.tmpReg2(), from2C.getName()));
            from2C.setName(Var.tmpReg2());
        }

        sb.append(String.format("div %s, %s\n", from1C.getName(), from2C.getName()));

        if (toC.getVarType() == VarType.REG) {
            sb.append(String.format("mfhi %s\n", toC.getName()));
        } else {
            sb.append(String.format("mfhi %s\n", Var.tmpReg1()));
            sb.append(String.format("sw %s, %s\n", Var.tmpReg1(), toC.getSpForm()));
        }



        return sb.toString();
    }

    @Override
    public String toString() {
        return "Mod{" +
                "to=" + to +
                ", from1=" + from1 +
                ", from2=" + from2 +
                '}';
    }
}
