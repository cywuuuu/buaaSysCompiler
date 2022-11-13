package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class GetInt extends IR{
    private Var to;

    public GetInt(Var to) {
        this.to = to;
        super.setIrType(IRType.GET_INT);
    }
    public Var getDef() {
        return to;
    }


    @Override
    public String toAsm() {
        if (to.getVarType() == VarType.REG) {
            return String.format("move %s, %s\n", to, Var.ioReg());
        } else {
            return String.format("sw %s, %s\n", Var.ioReg(), to.getSpForm());
        }
    }

    @Override
    public String toString() {
        return "GetInt{" +
                "to=" + to +
                '}';
    }
}
