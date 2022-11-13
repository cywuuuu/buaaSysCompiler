package Lexer;

import Nodes.Node;

public class Token extends Node {
    private TokenType type;
    private String token;
    private int line;
    public Token(TokenType type, String token, int line ) {
        super(line, line, type);
        this.type = type;
        this.token = token;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return token;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return type + " " + token;
    }
}
