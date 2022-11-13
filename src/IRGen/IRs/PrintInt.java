package IRGen.IRs;

import java.util.HashSet;

public class PrintInt extends IR{
    private Var toPrint;

    public PrintInt(Var toPrint) {
        this.toPrint = toPrint;
        super.setIrType(IRType.PRINT_INT);
    }


    public HashSet<Var> getUse() {
        HashSet<Var> a = new HashSet<>();
        if (toPrint.getVarType() != VarType.NUMBER)
            a.add(toPrint);
        return a;
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        if (toPrint.getVarType() == VarType.SP) {
            sb.append(String.format("lw %s, %s\n", Var.paramReg(0), toPrint.getSpForm()) );
        } else if (toPrint.getVarType() == VarType.NUMBER) {
            sb.append(String.format("li %s, %s\n", Var.paramReg(0), toPrint.getName()));
        } else {
            sb.append(String.format("move %s, %s\n", Var.paramReg(0), toPrint.getName()));
        }

        sb.append(String.format("li %s, 1\n", Var.ioReg()));
        sb.append("syscall\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "PrintInt{" +
                "toPrint=" + toPrint +
                '}';
    }
}
