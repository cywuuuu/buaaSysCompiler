package Parser.Error;

public class BreakContinueOutLoop extends ParserException{
    @Override
    public String toString() {
        return "m";
    }

    public BreakContinueOutLoop() {
    }

    public BreakContinueOutLoop(int line) {
        super(line);
    }
}
