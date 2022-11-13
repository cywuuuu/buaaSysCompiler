package IRGen.IRs;

import java.util.HashSet;

public class SetParam extends IR {
    private Var paramItem;
    private int num;

    public SetParam(Var paramItem, int num) {
        this.paramItem = paramItem;
        this.num = num;
        super.setIrType(IRType.SET_PARAM);
    }

    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (paramItem.getVarType() != VarType.NUMBER)
            a.add(paramItem);

        return a;
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        if (num < 4) {
            if (paramItem.getVarType() == VarType.ARRAY) { // tmp array stored in stack, so base get from $sp, pass base addr
                sb.append(String.format("addi %s, %s, %s\n", Var.paramReg(num), Var.spReg(), paramItem.getTmpArrayBase()));
            } else if (paramItem.getVarType() == VarType.SP) {
                sb.append(String.format("lw %s, %s\n", Var.paramReg(num), paramItem.getSpForm()));
            } else if (paramItem.getVarType() == VarType.REG) {
                sb.append(String.format("move %s, %s\n", Var.paramReg(num), paramItem.getName()));
            } else { // num
                sb.append(String.format("li %s, %s\n", Var.paramReg(num), paramItem.getName()));
            }
        } else {
            if (paramItem.getVarType() == VarType.REG) {
                sb.append(String.format("sw %s, %d($sp)\n", paramItem.getName(), -num * 4));

            } else {
                if (paramItem.getVarType() == VarType.SP) {
                    sb.append(String.format("lw %s, %s\n", Var.tmpReg1(), paramItem.getSpForm()));
                } else if (paramItem.getVarType() == VarType.ARRAY) {
                    sb.append(String.format("addi %s, %s, %s\n", Var.tmpReg1(),
                            Var.spReg(), paramItem.getTmpArrayBase()));
                } else { // num
                    sb.append(String.format("li %s, %s\n", Var.tmpReg1(), paramItem.getName()));
                }

                sb.append(String.format("sw %s, %d($sp)\n", Var.tmpReg1(), -num * 4));
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "SetParam{" +
                "paramItem=" + paramItem +
                ", num=" + num +
                '}';
    }
}
