package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class GetRet extends IR{
    private Var to;

    public GetRet(Var to) {
        this.to = to;
        super.setIrType(IRType.GETRET);
    }

    public Var getDef() {
        return to;
    }



    @Override
    public String toAsm() {

        if (to.getVarType() == VarType.SP) {
            return String.format("sw %s, %s\n", Var.ioReg(), to.getSpForm());
        } else {
            return String.format("move %s, %s\n", to.getName(), Var.ioReg());
        }

    }

    @Override
    public String toString() {
        return "GetRet{" +
                "to=" + to +
                '}';
    }
}
