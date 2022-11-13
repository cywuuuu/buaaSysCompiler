package Parser.Error;

public class UndefinedIdent extends ParserException{
    @Override
    public String toString() {
        return "c";
    }

    public UndefinedIdent() {
    }

    public UndefinedIdent(int line) {
        super(line);
    }
}
