package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class GetAddr extends IR {
    private Var to;
    private Var arr; // var / array /
    private Var i;

    public GetAddr(Var to, Var arr, Var i) {
        this.to = to;
        this.arr = arr;
        this.i = i;
        super.setIrType(IRType.GETADDR);
    }


    public Var getDef() {
        return to;
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (arr.getVarType() != VarType.NUMBER)
            a.add(arr);
        if (i.getVarType() != VarType.NUMBER)
            a.add(i);
        return a;
    }
    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        Var toC = to.deepCopy();
        Var arrC = arr.deepCopy();
        Var iC = i.deepCopy();
        // turn ir to reg
        if (arrC.getVarType() == VarType.ARRAY) {
            // tmp array stored in stack, so base get from $sp
            sb.append(String.format("addi %s, %s, %s\n", Var.tmpReg1(), Var.spReg(), arrC.getTmpArrayBase()));
            arrC.setName(Var.tmpReg1());
        } else if (arrC.getVarType() == VarType.GLOBVAR) { // global var array_xxx:, 取数组.data首地址
            sb.append(String.format("la %s, %s\n", Var.tmpReg1(), arrC));
            arrC.setName(Var.tmpReg1());
        } else if (arrC.getVarType() == VarType.SP) { // arr base is stored in stack
            sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), arrC.getSpForm()));
            arrC.setName(Var.tmpReg1());
        }

        if (iC.getVarType() == VarType.NUMBER) {
            iC.setName(String.valueOf((Integer.parseInt(i.getName()) * 4)));
        } else {
            if (iC.getVarType() == VarType.SP) {
                sb.append(String.format("lw %s, %s\n", Var.tmpReg2(), iC.getSpForm()));
                iC.setName(Var.tmpReg2());

            }
            sb.append(String.format("sll %s, %s, %d\n", Var.tmpReg2(), Var.tmpReg2(), 2));
            iC.setName(Var.tmpReg2());
        }

        if (toC.getVarType() == VarType.SP) {
            sb.append(String.format("addu %s, %s, %s\n", Var.tmpReg1(), arrC, iC.getName()));
            sb.append(String.format("sw %s, %s\n", Var.tmpReg1(), toC.getSpForm()));
        } else if (toC.getVarType() == VarType.REG) {
            sb.append(String.format("addu %s, %s, %s\n", toC.getName(), arrC, iC.getName()));
        }


        return sb.toString();
    }

    @Override
    public String toString() {
        return "GetAddr{" +
                "to=" + to +
                ", arr=" + arr +
                ", i=" + i +
                '}';
    }
}
