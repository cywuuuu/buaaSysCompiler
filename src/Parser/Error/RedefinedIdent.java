package Parser.Error;

public class RedefinedIdent extends ParserException{
    @Override
    public String toString() {
        return "b";
    }

    public RedefinedIdent() {
    }

    public RedefinedIdent(int line) {
        super(line);
    }
}
