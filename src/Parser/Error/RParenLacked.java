package Parser.Error;

public class RParenLacked extends ParserException{
    @Override
    public String toString() {
        return "j";
    }

    public RParenLacked() {
    }

    public RParenLacked(int line) {
        super(line);
    }
}
