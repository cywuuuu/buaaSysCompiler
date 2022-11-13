package IRGen.IRs;

import java.util.HashSet;

public class PrintStr extends IR{
    private Var toPrint;

    public PrintStr(Var toPrint) {
        this.toPrint = toPrint;
        super.setIrType(IRType.PRINT_STR);
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
        sb.append(String.format("la %s, %s\n", Var.paramReg(0), toPrint.getName()));
        sb.append("li $v0, 4\n");
        sb.append("syscall\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "PrintStr{" +
                "toPrint=" + toPrint +
                '}';
    }
}
