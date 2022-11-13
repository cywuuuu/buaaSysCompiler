package Lexer;

import java.util.HashMap;

public enum TokenType {
    MAIN,
    CONST,
    INT,
    BREAK,
    CONTINUE,
    IF,
    ELSE,
    WHILE,
    GETINT,
    PRINTF,
    RETURN,
    VOID,

    IDENT,
    INT_CONST,
    FMT_STR,

    NOT,
    AND,
    OR,

    PLUS,
    MINUS,
    MULT,
    DIV,
    MOD,

    LSS,
    LEQ,
    GRE,
    GEQ,
    EQL,
    NEQ,

    ASSIGN,
    SEMICN,
    COMMA,

    LPARENT,
    RPARENT,
    LBRACK,
    RBRACK,
    LBRACE,
    RBRACE,

    //extra
    BLOCK_COMMENT,
    LINE_COMMENT,

    //syntax token
    CompUnit,
    ConstDecl,
    VarDecl,
    ConstDef,
    ConstInitVal,
    VarDef,
    InitVal,
    FuncDef,
    MainFuncDef,
    FuncType,
    FuncFParams,
    FuncFParam,
    Block,
    Stmt,
    Exp,
    Cond,
    LVal,
    PrimaryExp,
    Number,
    UnaryExp,
    UnaryOp,
    FuncRParams,
    MulExp,
    AddExp,
    RelExp,
    EqExp,
    LAndExp,
    LOrExp,
    ConstExp,
    Error
    ;


    static HashMap<String, TokenType> strKeyword = new HashMap<String, TokenType>() {{
        put("main", MAIN);
        put("const", CONST);
        put("int", INT);
        put("break", BREAK);
        put("continue", CONTINUE);
        put("if", IF);
        put("else", ELSE);
        put("while", WHILE);
        put("getint", GETINT);

        put("printf", PRINTF);
        put("return", RETURN);
        put("void", VOID);

    }};

    @Override
    public String toString() {
        HashMap<TokenType, String> token2Name = new HashMap<TokenType, String>() {{
            put(IDENT, "IDENFR");
            put(INT_CONST, "INTCON");
            put(FMT_STR, "STRCON");
            put(MAIN, "MAINTK");
            put(CONST, "CONSTTK");
            put(INT, "INTTK");
            put(BREAK, "BREAKTK");
            put(CONTINUE, "CONTINUETK");
            put(IF, "IFTK");
            put(ELSE, "ELSETK");
            put(NOT, "NOT");
            put(AND, "AND");
            put(OR, "OR");
            put(WHILE, "WHILETK");
            put(GETINT, "GETINTTK");
            put(PRINTF, "PRINTFTK");
            put(RETURN, "RETURNTK");
            put(PLUS, "PLUS");
            put(MINUS, "MINU");
            put(VOID, "VOIDTK");
            put(MULT, "MULT");
            put(DIV, "DIV");
            put(MOD, "MOD");
            put(LSS, "LSS");
            put(LEQ, "LEQ");
            put(GRE, "GRE");
            put(GEQ, "GEQ");
            put(EQL, "EQL");
            put(NEQ, "NEQ");
            put(ASSIGN, "ASSIGN");
            put(SEMICN, "SEMICN");
            put(COMMA, "COMMA");
            put(LPARENT, "LPARENT");
            put(RPARENT, "RPARENT");
            put(LBRACK, "LBRACK");
            put(RBRACK, "RBRACK");
            put(LBRACE, "LBRACE");
            put(RBRACE, "RBRACE");
        }};
        if (token2Name.containsKey(this)) {
            return token2Name.get(this);
        } else {
            return this.name();
        }

    }
}


