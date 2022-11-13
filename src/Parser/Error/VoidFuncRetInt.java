package Parser.Error;

public class VoidFuncRetInt extends ParserException{
    @Override
    public String toString() {
        return "f";
    }

    public VoidFuncRetInt() {
    }

    public VoidFuncRetInt(int line) {
        super(line);
    }
}
