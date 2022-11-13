package Parser.Error;

public class IntFuncRetLacked extends ParserException{
    @Override
    public String toString() {
        return "g";
    }

    public IntFuncRetLacked() {
    }

    public IntFuncRetLacked(int line) {
        super(line);
    }
}
