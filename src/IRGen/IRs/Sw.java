package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class Sw extends IR{
    private Var from;
    private Var arr; // var / array /
    private Var i;

    public Sw(Var from, Var arr, Var i) {
        this.from = from;
        this.arr = arr;
        this.i = i;
        super.setIrType(IRType.SW);
    }


    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (arr.getVarType() != VarType.NUMBER)
            a.add(arr);
        if (i.getVarType() != VarType.NUMBER)
            a.add(i);
        if (from.getVarType() != VarType.NUMBER)
            a.add(from);
        return a;
    }

    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        Var fromC = from.deepCopy();
        Var arrC = arr.deepCopy();
        Var iC = i.deepCopy();
        // turn i to reg
        if (arrC.getVarType() == VarType.ARRAY) {
            // tmp array stored in stack, so base get from $sp
            sb.append(String.format("addi %s, %s, %s\n", Var.tmpReg1(), Var.spReg(), arrC.getTmpArrayBase()));
            arrC.setName(Var.tmpReg1());
        } else if (arrC.getVarType() == VarType.GLOBVAR) { // global var array_xxx:
            sb.append(String.format("la %s, %s\n", Var.tmpReg1(), arrC.getName()));
            arrC.setName(Var.tmpReg1());
        } else if (arrC.getVarType() == VarType.SP) { // arr base is stored in stack
            sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), arrC.getSpForm()));
            arrC.setName(Var.tmpReg1());
        }

        // iC to reg
        if (iC.getVarType() == VarType.NUMBER) {
            iC.setName(String.valueOf((Integer.parseInt(i.toString()) * 4)));
        } else {
            if (iC.getVarType() == VarType.SP) {
                sb.append(String.format("lw %s, %s\n", Var.tmpReg2(), iC.getSpForm()));
                iC.setName(Var.tmpReg2());

            }
            sb.append(String.format("sll %s, %s, %d\n", Var.tmpReg2(), iC.getName(), 2));
            sb.append(String.format("addu %s, %s, %s\n", Var.tmpReg1(), arrC.getName(), Var.tmpReg2()));
            iC.setName("0");
            arrC.setName(Var.tmpReg1());
        }

        if (fromC.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.tmpReg2(), fromC.getSpForm()));
            sb.append(String.format("sw %s, %s(%s)\n", Var.tmpReg2(), iC.getName(), arrC.getName()));
        } else if (fromC.getVarType() == VarType.REG) {
            sb.append(String.format("sw %s, %s(%s)\n", fromC.getName(), iC.getName(), arrC.getName()));
        } else { // NUMBER
            sb.append(String.format("li %s, %s\n", Var.tmpReg2(), fromC.getName()));
            sb.append(String.format("sw %s, %s(%s)\n", Var.tmpReg2(), iC.getName(), arrC.getName()));
        }




        return sb.toString();
    }

    @Override
    public String toString() {
        return "Sw{" +
                "from=" + from +
                ", arr=" + arr +
                ", i=" + i +
                '}';
    }
}
