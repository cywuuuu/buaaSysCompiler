package Parser.Error;

public class ParamsNumUnmatched extends ParserException{
    @Override
    public String toString() {
        return "d";
    }

    public ParamsNumUnmatched() {
    }

    public ParamsNumUnmatched(int line) {
        super(line);
    }
}
