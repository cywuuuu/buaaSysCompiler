package Parser.Error;

public class RBrackLacked extends ParserException{
    @Override
    public String toString() {
        return "k";
    }

    public RBrackLacked() {
    }

    public RBrackLacked(int line) {
        super(line);
    }
}
