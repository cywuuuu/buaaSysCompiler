package Parser.Error;

public class UndefinedError extends ParserException{
    @Override
    public String toString() {
        return "UndefinedError{}";
    }

    public UndefinedError() {
    }

    public UndefinedError(int line) {
        super(line);
    }
}
