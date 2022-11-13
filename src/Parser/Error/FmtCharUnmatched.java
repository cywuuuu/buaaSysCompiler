package Parser.Error;

public class FmtCharUnmatched extends ParserException{
    @Override
    public String toString() {
        return "l";
    }

    public FmtCharUnmatched() {
    }

    public FmtCharUnmatched(int line) {
        super(line);
    }
}
