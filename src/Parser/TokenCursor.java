package Parser;

import Lexer.Token;
import Lexer.TokenType;

import java.io.EOFException;
import java.util.ArrayList;

public class TokenCursor {
    ArrayList<Token> tokens;
    int pos = 0;

    public TokenCursor(ArrayList<Token> tokens) {
        this.tokens = tokens;

    }

    public Token nowToken() throws EOFException {
        try {
            return tokens.get(pos);
        } catch (IndexOutOfBoundsException e) {
            throw new EOFException();
        }

    }

    public Token peekNextNToken(int n) throws EOFException {

        try {
            return tokens.get(n + pos);
        } catch (IndexOutOfBoundsException e) {
            throw new EOFException();
        }
    }

    public Token consumeToken() throws EOFException {
        try {
            Token t = tokens.get(pos);
            pos++;
            return t;
        } catch (IndexOutOfBoundsException e) {
            throw new EOFException();
        }

    }

    public int atLine() throws EOFException {
        return nowToken().getLine();
    }
    public int prevTokenAtLine() throws EOFException {
        return peekNextNToken(-1).getLine();
    }
}
