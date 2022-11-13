package IRGen.IRs;

public class ReadInt extends IR{
    public ReadInt() {
        super.setIrType(IRType.READ_INT);
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        sb.append("li $v0, 5\n");
        sb.append("syscall\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ReadInt{" +

                '}';
    }
}
