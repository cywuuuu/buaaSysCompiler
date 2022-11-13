package Lexer;

import java.io.EOFException;
import java.util.regex.Pattern;

public class Lexer {
    Cursor cursor;
    // 新增元素，首先加lexer解析token
    // 1. TokenType 中更新名字名称（包括非终结符）
    // 2. lexer写解析逻辑
    // parser
    // Node 可能要新增对应语法成分，astbuilder InsertNode 里加入工厂
    // 如果新增在 (|....|....|) 中要考虑对整体的first集修改以保证上面落进去，对本身判别选项分支也要分清
    public Lexer(Cursor cursor) {
        this.cursor = cursor;
    }

    public Token getToken() throws EOFException, LexicalException {
        int l = cursor.getPos();
        char a = cursor.consumeChar();
        char b = cursor.nowChar();
        TokenType type;
        switch (a) {
            case '!':
                if (b == '=') {
                    cursor.consumeChar();
                    type = TokenType.NEQ;
                } else {
                    type = TokenType.NOT;
                }
                break;
            case '&':
                if (b == '&') {
                    cursor.consumeChar();
                    type = TokenType.AND;
                } else { // &X => &' ', &1
                    throw new LexicalException();
                }
                break;
            case '|':
                if (b == '|') {
                    cursor.consumeChar();
                    type = TokenType.OR;
                } else { // &X => &' ', &1
                    throw new LexicalException();
                }
                break;
            case '+':
                type = TokenType.PLUS;
                break;
            case '-':
                type = TokenType.MINUS;
                break;
            case '*':
                type = TokenType.MULT;
                break;
            case '/':
                if (b == '/') {
                    cursor.consumeChar(); // pass /
                    while (cursor.nowChar() != '\n') {
                        cursor.consumeChar();
                    }
                    cursor.consumeChar(); // pass \n
                    type = TokenType.LINE_COMMENT;
                } else if (b == '*') {
                    cursor.consumeChar(); // pass *
                    cursor.consumeChar(); // pass x
                    do {
                        char t2 = cursor.consumeChar();//check if /
                        char t1 = cursor.peekCharPrev(2);// check if
                        if ((t1 == '*' && t2 == '/')) {
                            break;
                        }
                    } while (true);
                    type = TokenType.BLOCK_COMMENT;
                } else {
                    type = TokenType.DIV;
                }
                break;
            case '%':
                type = TokenType.MOD;
                break;
            case '<':
                if (b == '=') {
                    cursor.consumeChar();
                    type = TokenType.LEQ;
                } else {
                    type = TokenType.LSS;
                }
                break;
            case '>':
                if (b == '=') {
                    cursor.consumeChar();
                    type = TokenType.GEQ;
                } else {
                    type = TokenType.GRE;
                }
                break;
            case '=':
                if (b == '=') {
                    cursor.consumeChar();
                    type = TokenType.EQL;
                } else {
                    type = TokenType.ASSIGN;
                }
                break;
            case ';':
                type = TokenType.SEMICN;
                break;
            case ',':
                type = TokenType.COMMA;
                break;
            case '(':
                type = TokenType.LPARENT;
                break;
            case ')':
                type = TokenType.RPARENT;
                break;
            case '[':
                type = TokenType.LBRACK;
                break;
            case ']':
                type = TokenType.RBRACK;
                break;
            case '{':
                type = TokenType.LBRACE;
                break;
            case '}':
                type = TokenType.RBRACE;
                break;
            case '"':
                //pass "
                while (true) {
                    char t = cursor.consumeChar();
                    if (t == '"') {
                        type = TokenType.FMT_STR;
                        break;
                    }
                }
                break;
            default: //ident or integer
                if (isdigit(a)) {
                    while (isdigit(cursor.nowChar())) {
                        cursor.consumeChar();
                    }
                    type = TokenType.INT_CONST;
                } else if (isNonDigit(a)) {
                    StringBuilder ident = new StringBuilder(String.valueOf(a));
                    char t;//n
                    while (true) {
                        t = cursor.nowChar();//
                        if (!(isNonDigit(t) || isdigit(t))) {
                            break;
                        }
                        ident.append(t);
                        cursor.consumeChar(); //pass n, t
                    }
                    if (TokenType.strKeyword.containsKey(ident.toString())) {
                        type = TokenType.strKeyword.get(ident.toString());
                    } else {
                        type = TokenType.IDENT;
                    }
                } else {
                    throw new LexicalException(); 
                }

        }
        int r = cursor.getPos();
        return new Token(type, cursor.pos2Str(l, r), cursor.getLineNum());
    }

    private boolean isdigit(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3'
                || c == '4' || c == '5' || c == '6'
                || c == '7' || c == '8' || c == '9';
    }

    private boolean isNonDigit(char c) {
        String s = String.valueOf(c);
        return Pattern.matches("^[a-zA-Z_]$", s);
    }

    public Token consumeToken() throws EOFException, LexicalException {

        while (Character.isSpace(cursor.nowChar())) {
            cursor.consumeChar();
        }
        Token token = getToken();
        while (token.getType() == TokenType.LINE_COMMENT || token.getType() == TokenType.BLOCK_COMMENT) {
            while (Character.isSpace(cursor.nowChar())) {
                cursor.consumeChar();
            }
            token = getToken();
        }
        return token;
    }


}
