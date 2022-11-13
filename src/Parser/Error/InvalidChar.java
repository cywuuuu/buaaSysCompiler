package Parser.Error;

public class InvalidChar extends ParserException{
    @Override
    public String toString() {
        return "a";
    }

    public InvalidChar() {
    }

    public InvalidChar(int line) {
        super(line);
    }
}
