package IRGen.IRs;

import IRGen.CFG.BBlock;

public class Jmp extends IR{
    BBlock target;

    public Jmp(BBlock target) {
        this.target = target;
        super.setIrType(IRType.JMP);
    }

    @Override
    public String toAsm() {
        return String.format("j %s\n", target.getLabel());
    }

    @Override
    public String toString() {
        return "Jmp{" +
                "target=" + target.getLabel() +
                '}';
    }
}
