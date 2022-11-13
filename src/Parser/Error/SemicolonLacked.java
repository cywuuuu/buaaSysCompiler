package Parser.Error;

public class SemicolonLacked extends ParserException{
    @Override
    public String toString() {
        return "i";
    }

    public SemicolonLacked() {
    }

    public SemicolonLacked(int line) {
        super(line);
    }
}
