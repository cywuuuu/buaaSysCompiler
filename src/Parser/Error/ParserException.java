package Parser.Error;

public class ParserException extends Exception{
    int line;
    @Override
    public String toString() {
        return "ParserException{}";
    }

    public ParserException() {
    }

    public ParserException(int line) {
        this.line = line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
