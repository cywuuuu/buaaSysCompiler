package Lexer;

import java.io.EOFException;
import java.util.function.Function;

public class Cursor {
    private int pos;
    private Source source;

    int getPos() {
        return pos;
    }

    int getLineNum() {
        return source.char2lineNum(pos - source.begin());
    }

    public Cursor(Source source) {
        pos = source.begin();
        this.source = source;
    }

    public char peekCharPrev(int n) throws EOFException {
        try {
            return source.charAt(pos - n);
        } catch (StringIndexOutOfBoundsException e) {
            throw new EOFException();
        }


    }

    public char peekCharNext(int n) throws EOFException {
        try {
            return source.charAt(pos + n);
        } catch (StringIndexOutOfBoundsException e) {
            throw new EOFException();
        }


    }

    public char consumeChar() throws EOFException {
        char c;
        try {
            c = source.charAt(pos);
        } catch (StringIndexOutOfBoundsException e) {
            throw new EOFException();
        }

        if (pos >= source.end()) {
            pos = pos;
            throw new EOFException();
        } else {
            pos++;
        }
        return c;
    }

    public char nowChar() throws EOFException {
        return peekCharNext(0);
    }

    public String pos2Str(int l, int r) throws EOFException {

        try {
            return source.pos2Str(l, r);
        } catch (StringIndexOutOfBoundsException e) {
            throw new EOFException();
        }
    }


}
