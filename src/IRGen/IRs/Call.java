package IRGen.IRs;

public class Call extends IR{
    Var funcLabel;

    public Call(Var funcLabel) {
        this.funcLabel = funcLabel;
        super.setIrType(IRType.CALL);
    }

    @Override
    public String toAsm() {
        return String.format("jal %s\n", funcLabel);
    }

    @Override
    public String toString() {
        return "Call{" +
                "funcLabel=" + funcLabel.getName() +
                '}';
    }
}
