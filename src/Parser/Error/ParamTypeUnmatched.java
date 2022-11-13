package Parser.Error;

public class ParamTypeUnmatched extends ParserException{
    @Override
    public String toString() {
        return "e";
    }

    public ParamTypeUnmatched() {
    }

    public ParamTypeUnmatched(int line) {
        super(line);
    }
}
