package IRGen.IRs;

import java.util.Collections;
import java.util.HashSet;

public class GetParam extends IR{
    private Var to;
    private int num; //$a_num
    public GetParam(Var to, int num) {
        this.to = to;
        this.num = num;
        super.setIrType(IRType.GET_PARAM);
    }

    public Var getDef() {
        return to;
    }



    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        if (num < 4) {
            if (to.getVarType() == VarType.SP) {
                sb.append(String.format("sw %s, %s\n", Var.paramReg(num), to.getSpForm()));
            } else { //
                sb.append(String.format("move %s, %s\n", to, Var.paramReg(num)));
            }
        } else {
            if (to.getVarType() == VarType.SP) {
                // 在之前sp压栈了，要补上，来获取
                sb.append(String.format("lw %s, %d($sp)\n", Var.tmpReg1(), inFunc.getFrameSize()-num*4));
                sb.append(String.format("sw %s, %s\n", Var.tmpReg1(), to.getSpForm()));
            } else {
                sb.append(String.format("lw %s, %d($sp)\n", to.getName(), inFunc.getFrameSize()-num*4));
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "GetParam{" +
                "to=" + to +
                ", num=" + num +
                '}';
    }
}
