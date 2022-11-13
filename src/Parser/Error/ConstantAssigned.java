package Parser.Error;

public class ConstantAssigned extends ParserException{
    @Override
    public String toString() {
        return "h";
    }

    public ConstantAssigned(int line) {
        super(line);
    }

    public ConstantAssigned() {
    }
}
