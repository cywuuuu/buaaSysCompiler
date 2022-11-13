package Parser;

import Lexer.Token;
import Lexer.TokenType;
import Nodes.Node;
import Nodes.Stmt;
import Nodes.UnaryExp;
import Parser.Error.ParserException;
import Parser.Error.RBrackLacked;
import Parser.Error.RParenLacked;
import Parser.Error.SemicolonLacked;
import Parser.Error.UndefinedError;

import java.io.EOFException;
import java.util.ArrayList;


public class Parser {

    private AstBuilder astBuilder;
    private TokenCursor cursor;

    public Parser(ArrayList<Token> tokens) {
        this.astBuilder = new AstBuilder();
        this.cursor = new TokenCursor(tokens);
    }

    public Node astRoot() {
        return astBuilder.root();
    }

    private void checkToken(TokenType type, ParserException e) throws ParserException, EOFException {
        if (cursor.nowToken().getType() != type) {
            if (e instanceof SemicolonLacked || e instanceof RParenLacked || e instanceof  RBrackLacked) {
                astBuilder.error(e, cursor.prevTokenAtLine());
            } else {
                astBuilder.error(e, cursor.atLine()); //line待改
            }

            if (e instanceof UndefinedError) throw e;
        } else {
            parseTerm();
        }
    }

    // CompUnit → {Decl} {FuncDef} MainFuncDef
    public void parseCompUnit() throws ParserException {
        try {

            Node compUnit = astBuilder.startBld(TokenType.CompUnit, cursor.atLine());
            while (true) {
                if (cursor.nowToken().getType() == TokenType.CONST) {
                    // {ConstDecl}
                    parseConstDecl();
                } else {
                    if (cursor.peekNextNToken(2).getType() == TokenType.LPARENT) {
                        break;
                    } else {
                        parseVarDecl();
                    }
                }
            }
            while (true) {
                if (cursor.peekNextNToken(1).getType() == TokenType.MAIN) {
                    break;
                } else if (cursor.peekNextNToken(2).getType() == TokenType.LPARENT) {
                    parseFuncDef();
                } else {
                    throw new ParserException();
                }
            }
            parseMainFuncDef();

            astBuilder.endBld(compUnit, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    public void parseConstDecl() throws ParserException {
        try {
            Node constDecl = astBuilder.startBld(TokenType.ConstDecl, cursor.atLine());
            checkToken(TokenType.CONST, new UndefinedError());
            checkToken(TokenType.INT, new UndefinedError());
            parseConstDef();

            while (cursor.nowToken().getType() == TokenType.COMMA) {
                parseTerm();// pass ,
                if (cursor.nowToken().getType() == TokenType.IDENT) {
                    parseConstDef();
                } else {
                    throw new UndefinedError();
                }
            }

            checkToken(TokenType.SEMICN, new SemicolonLacked());
            astBuilder.endBld(constDecl, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    // VarDecl → BType VarDef { ',' VarDef } ';'
    public void parseVarDecl() throws ParserException {
        try {
            Node varDecl = astBuilder.startBld(TokenType.VarDecl, cursor.atLine());
            checkToken(TokenType.INT, new UndefinedError());
            parseVarDef();

            while (cursor.nowToken().getType() == TokenType.COMMA) {
                parseTerm();// pass ,
                if (cursor.nowToken().getType() == TokenType.IDENT) {
                    parseVarDef();
                } else {
                    throw new UndefinedError();
                }
            }

            checkToken(TokenType.SEMICN, new SemicolonLacked());
            astBuilder.endBld(varDecl, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    public void parseConstDef() throws ParserException {

        try {
            Node constDef = astBuilder.startBld(TokenType.ConstDef, cursor.atLine());
            checkToken(TokenType.IDENT, new UndefinedError());

            while (cursor.nowToken().getType() == TokenType.LBRACK) {

//                Token constExp = cursor.consumeToken();
                parseTerm();
                parseConstExp();
                //                if (cursor.nowToken().getType() != TokenType.RBRACK) {
                //                    throw new ParserException();
                //                }
                checkToken(TokenType.RBRACK, new RBrackLacked());
            }

            checkToken(TokenType.ASSIGN, new UndefinedError());
            parseConstInitVal();

            astBuilder.endBld(constDef, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }


    //  InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    public void parseConstInitVal() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.ConstInitVal, cursor.atLine());
            if (cursor.nowToken().getType() == TokenType.LBRACE) {
                parseTerm();
                if (cursor.nowToken().getType() == TokenType.RBRACE) {
                    parseTerm();
                } else {
                    parseConstInitVal();
                    while (cursor.nowToken().getType() == TokenType.COMMA) {
                        parseTerm();
                        parseConstInitVal();
                    }
                    checkToken(TokenType.RBRACE, new UndefinedError());
                }

            } else {
                parseConstExp();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }


    private void parseTerm() throws EOFException {
        astBuilder.insertTerm(cursor.nowToken());
        Token t = cursor.consumeToken();
        // System.out.println(t);
    }

    //VarDef → Ident { '[' ConstExp ']' } // 包含普通变量、一维数组、二维数组定义
    //       | Ident { '[' ConstExp ']' } '=' InitVal
    public void parseVarDef() throws ParserException {
        try {
            Node varDef = astBuilder.startBld(TokenType.VarDef, cursor.atLine());
            checkToken(TokenType.IDENT, new UndefinedError());

            while (cursor.nowToken().getType() == TokenType.LBRACK) {

//                Token constExp = cursor.consumeToken();
                parseTerm();
                parseConstExp();
//                if (cursor.nowToken().getType() != TokenType.RBRACK) {
//                    throw new ParserException();
//                }
                checkToken(TokenType.RBRACK, new RBrackLacked());
            }
            if (cursor.nowToken().getType() == TokenType.ASSIGN) {
                parseTerm();
                parseInitVal();
            }
            astBuilder.endBld(varDef, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    //  InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    public void parseInitVal() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.InitVal, cursor.atLine());
            if (cursor.nowToken().getType() == TokenType.LBRACE) {
                parseTerm();
                if (cursor.nowToken().getType() == TokenType.RBRACE) {
                    parseTerm();
                } else {
                    parseInitVal();
                    while (cursor.nowToken().getType() == TokenType.COMMA) {
                        parseTerm();
                        parseInitVal();
                    }
                    checkToken(TokenType.RBRACE, new UndefinedError());
                }

            } else {
                parseExp();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    public void parseFuncDef() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.FuncDef, cursor.atLine());
            parseFuncType();
            parseTerm();// ident funcname
            checkToken(TokenType.LPARENT, new UndefinedError());
            if (cursor.nowToken().getType() == TokenType.INT) {
                parseFuncFParams();
            }
            checkToken(TokenType.RPARENT, new RParenLacked());
            parseBlock();
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    public void parseMainFuncDef() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.MainFuncDef, cursor.atLine());
            checkToken(TokenType.INT, new UndefinedError());

            checkToken(TokenType.MAIN, new UndefinedError());

            checkToken(TokenType.LPARENT, new UndefinedError());

            checkToken(TokenType.RPARENT, new RParenLacked());

            parseBlock();
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    // FuncType → 'void' | 'int'
    public void parseFuncType() throws ParserException {
        try {
            Node funcType = astBuilder.startBld(TokenType.FuncType, cursor.atLine());
            if (cursor.nowToken().getType() != TokenType.INT && cursor.nowToken().getType() != TokenType.VOID) {
                throw new ParserException();
            }
            parseTerm();
            astBuilder.endBld(funcType, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    public void parseFuncFParams() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.FuncFParams, cursor.atLine());
            parseFuncFParam();
            while (cursor.nowToken().getType() == TokenType.COMMA) {
                parseTerm();
                parseFuncFParam();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    public void parseFuncFParam() throws ParserException {

        try {
            Node node = astBuilder.startBld(TokenType.FuncFParam, cursor.atLine());
            checkToken(TokenType.INT, new UndefinedError());

            checkToken(TokenType.IDENT, new UndefinedError());

            if (cursor.nowToken().getType() == TokenType.LBRACK) {
                parseTerm();
                checkToken(TokenType.RBRACK, new RBrackLacked());
            }

            while (cursor.nowToken().getType() == TokenType.LBRACK) {
                parseTerm();
                parseConstExp();
                checkToken(TokenType.RBRACK, new RBrackLacked());
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    // Block → '{' { ConstDecl | VarDecl | stmt } '}'
    public void parseBlock() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.Block, cursor.atLine());
            checkToken(TokenType.LBRACE, new UndefinedError());

            while (cursor.nowToken().getType() != TokenType.RBRACE) {
                //?
                if (cursor.nowToken().getType() == TokenType.CONST) {
                    parseConstDecl();
                } else if (cursor.nowToken().getType() == TokenType.INT) {
                    parseVarDecl();
                } else {
                    parseStmt();
                }
            }
            checkToken(TokenType.RBRACE, new UndefinedError());
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    /**
     * Stmt → LVal '=' Exp ';' // 每种类型的语句都要覆盖
     * | [Exp] ';' //有无Exp两种情况
     * | Block
     * | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.无else
     * | 'while' '(' Cond ')' Stmt
     * | 'break' ';'
     * | 'continue' ';'
     * | 'return' [Exp] ';' // 1.有Exp 2.无Exp
     * | LVal '=' 'getint''('')'';'
     * | 'printf''('FormatString{','Exp}')'';' // 1.有Exp 2.无Exp
     */
    public void parseStmt() throws ParserException {
        UndefinedError e = new UndefinedError();
        try {
            Node node = astBuilder.startBld(TokenType.Stmt, cursor.atLine());
            String stmtType = null;
            if (cursor.nowToken().getType() == TokenType.LBRACE) {
                parseBlock();
                stmtType = "Block";
            } else if (cursor.nowToken().getType() == TokenType.WHILE) {

                checkToken(TokenType.WHILE, e);// pass while

                checkToken(TokenType.LPARENT, e);// pass (

                parseCond();

                checkToken(TokenType.RPARENT, new RParenLacked());// pass )

                parseStmt();
                stmtType = "While";
            } else if (cursor.nowToken().getType() == TokenType.IF) {
                checkToken(TokenType.IF, e); //parse if
                checkToken(TokenType.LPARENT, e);// pass (

                parseCond();

                checkToken(TokenType.RPARENT, new RParenLacked());// pass )
                parseStmt();

                if (cursor.nowToken().getType() == TokenType.ELSE) {
                    parseTerm();// parse else
                    parseStmt();
                }
                stmtType = "If";
            } else if (cursor.nowToken().getType() == TokenType.BREAK) {
                checkToken(TokenType.BREAK, e);
                checkToken(TokenType.SEMICN, new SemicolonLacked());
                stmtType = "Break";
            } else if (cursor.nowToken().getType() == TokenType.CONTINUE) {
                checkToken(TokenType.CONTINUE, e);
                checkToken(TokenType.SEMICN, new SemicolonLacked());
                stmtType = "Continue";
            } else if (cursor.nowToken().getType() == TokenType.RETURN) {
                checkToken(TokenType.RETURN, e);// pass return
                if (cursor.nowToken().getType() == TokenType.SEMICN) {
                    parseTerm(); // pass ;
                } else {
                    if (checkExprFirst(cursor.nowToken())) { // 有EXP
                        // checkvoid ?
                        parseExp(); // pass exp
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    } else { // 无EXP
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    }
                }
                stmtType = "Return";
                // * | 'printf''('FormatString{','Exp}')'';' // 1.有Exp 2.无Exp
            } else if (cursor.nowToken().getType() == TokenType.PRINTF) {
                checkToken(TokenType.PRINTF, e);
                checkToken(TokenType.LPARENT, e);
                checkToken(TokenType.FMT_STR, e);
                while (cursor.nowToken().getType() == TokenType.COMMA) {
                    parseTerm();//pass ,
                    parseExp();
                }
                checkToken(TokenType.RPARENT, new RParenLacked());
                checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                stmtType = "Printf";
            }
            //  | LVal '=' Exp ';' // 每种类型的语句都要覆盖
            //  | LVal '=' 'getint''('')'';'
            //  | [Exp] ';' //有无Exp两种情况

            else if (cursor.nowToken().getType() == TokenType.IDENT) {
                int pos = preReadLVal(0);
                if (cursor.peekNextNToken(pos).getType() == TokenType.ASSIGN) {
                    parseLVal();
                    //  | LVal '=' Exp ';' // 每种类型的语句都要覆盖
                    //  | LVal '=' 'getint''('')'';'
                    checkToken(TokenType.ASSIGN, e);

                    if (cursor.nowToken().getType() == TokenType.GETINT) {
                        parseTerm(); //pass getint
                        checkToken(TokenType.LPARENT, e);
                        checkToken(TokenType.RPARENT, new RParenLacked());
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    } else {
                        parseExp();
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    }
                    stmtType = "Assign";
                } else {
                    // | [Exp] ';' //有无Exp两种情况
                    if (cursor.nowToken().getType() == TokenType.SEMICN) {
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    } else {
                        parseExp();
                        checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                    }
                    stmtType = "Expr";
                }
            } else {
                // | [Exp] ';' //有无Exp两种情况
                if (cursor.nowToken().getType() == TokenType.SEMICN) {
                    checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                } else {
                    parseExp();
                    checkToken(TokenType.SEMICN, new SemicolonLacked()); // pass ;
                }
                stmtType = "Expr";
            }
            ((Stmt)node).setStmtType(stmtType);
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException ee) {
            ;
        }
    }

    // Ident {'[' Exp ']'}
    private int preReadLVal(int base) throws EOFException {
        if (cursor.peekNextNToken(base).getType() != TokenType.IDENT) {
            return -1;
        }
        if (cursor.peekNextNToken(base + 1).getType() == TokenType.LPARENT) {
            return -1;
        }
        int pos = 1;
        while (cursor.peekNextNToken(base + pos).getType() == TokenType.LBRACK) {
            pos++;
            pos += preReadExp(base + pos);
            if (cursor.peekNextNToken(base + pos).getType() == TokenType.RBRACK) {
                pos++;
            }
        }
        return pos;
    }

    private int preReadExp(int base) throws EOFException {
        return preReadAddExp(base);
    }
    // AddExp → MulExp | AddExp ('+' | '−') MulExp
    private int preReadAddExp(int base) throws EOFException {
        int pos = preReadMulExp(base);
        while (cursor.peekNextNToken(base + pos).getType() == TokenType.PLUS
                || cursor.peekNextNToken(base + pos).getType() == TokenType.MINUS) {
            pos++;
            pos += preReadMulExp(base + pos);
        }
        return pos;
    }
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    private int preReadMulExp(int base) throws EOFException {
        int pos = preReadUnaExp(base);
        while (cursor.peekNextNToken(base + pos).getType() == TokenType.MULT
                || cursor.peekNextNToken(base + pos).getType() == TokenType.DIV
                || cursor.peekNextNToken(base + pos).getType() == TokenType.MOD) {
            pos++;
            pos += preReadUnaExp(base + pos);
        }
        return pos;
    }
    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    private int preReadUnaExp(int base) throws EOFException {
        if (cursor.peekNextNToken(base).getType() == TokenType.IDENT
                && cursor.peekNextNToken(base + 1).getType() == TokenType.LPARENT) {
            if (cursor.peekNextNToken(base + 2).getType() == TokenType.RPARENT) {
                return 3;
            }
            if (checkExprFirst(cursor.peekNextNToken(base + 2))) {
                int pos = preReadFuncParams(base + 2) + 2;
                if (cursor.peekNextNToken(base + pos).getType() == TokenType.RPARENT) {
                    return pos + 1;
                } else {
                    return pos;
                }
            } else {
                return 2;
            }
        } else if (cursor.peekNextNToken(base).getType() == TokenType.PLUS
                || cursor.peekNextNToken(base).getType() == TokenType.MINUS
                || cursor.peekNextNToken(base).getType() == TokenType.NOT) {
            return 1 + preReadUnaExp(base + 1);
        } else {
            return preReadPrimExp(base);
        }
    }

    private int preReadFuncParams(int base) throws EOFException {
        int pos = preReadExp(base);
        while (cursor.peekNextNToken(base + pos).getType() == TokenType.COMMA) {
            pos++;
            pos += preReadExp(base);
        }
        return pos;
    }
    //  '(' Exp ')' | LVal | Number
    private int preReadPrimExp(int base) throws EOFException {
        if (cursor.peekNextNToken(base).getType() == TokenType.LPARENT) {
            return 2 + preReadExp(base + 1);
        } else if (cursor.peekNextNToken(base).getType() == TokenType.INT_CONST) {
            return 1;
        } else {
            return preReadLVal(base);
        }
    }


    private boolean checkExprFirst(Token token) {
        TokenType type = token.getType();
        return type == TokenType.IDENT || type == TokenType.PLUS
                || type == TokenType.MINUS || type == TokenType.NOT
                || type == TokenType.LPARENT || type == TokenType.INT_CONST;
    }

    private boolean checkStmtFirst(Token token) {
        TokenType type = token.getType();
        return type == TokenType.SEMICN || type == TokenType.LBRACE
                || type == TokenType.IF || type == TokenType.WHILE
                || type == TokenType.CONTINUE || type == TokenType.BREAK
                || type == TokenType.RETURN || type == TokenType.PRINTF
                || type == TokenType.IDENT || type == TokenType.LPARENT
                || type == TokenType.INT_CONST || type == TokenType.PLUS
                || type == TokenType.MINUS || type == TokenType.NOT;
    }


    public void parseExp() throws ParserException, EOFException {
        Node node = astBuilder.startBld(TokenType.Exp, cursor.atLine());
        parseAddExp();
        astBuilder.endBld(node, cursor.prevTokenAtLine());
    }

    public void parseCond() throws ParserException, EOFException {
        Node node = astBuilder.startBld(TokenType.Cond, cursor.atLine());
        parseLOrExp();
        astBuilder.endBld(node, cursor.prevTokenAtLine());
    }

    // LVal → Ident {'[' Exp ']'}
    public void parseLVal() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.LVal, cursor.atLine());
            checkToken(TokenType.IDENT, new UndefinedError());
            while (cursor.nowToken().getType() == TokenType.LBRACK) {
                parseTerm();
                parseExp();
                checkToken(TokenType.RBRACK, new RBrackLacked());
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    // PrimaryExp → '(' Exp ')' | LVal | Number
    public void parsePrimaryExp() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.PrimaryExp, cursor.atLine());
            if (cursor.nowToken().getType() == TokenType.LPARENT) {
                parseTerm();
                parseExp();
                checkToken(TokenType.RPARENT, new RParenLacked());

            } else if (cursor.nowToken().getType() == TokenType.INT_CONST) {
                parseNumber();
            } else {
                parseLVal();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    public void parseNumber() {
        try {
            Node node = astBuilder.startBld(TokenType.Number, cursor.atLine());
            parseTerm();
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' |  UnaryOp UnaryExp
    public void parseUnaryExp() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.UnaryExp, cursor.atLine());
            if (cursor.nowToken().getType() == TokenType.IDENT
                    && cursor.peekNextNToken(1).getType() == TokenType.LPARENT) {
                if (node instanceof UnaryExp) {
                    ((UnaryExp) node).setUnaType("Call");
                }
                // Ident '(' [FuncRParams] ')'
                parseTerm();// pass Ident
                parseTerm();// pass (

                if (cursor.nowToken().getType() != TokenType.RPARENT) {
                    //  [FuncRParams]
                    if (checkExprFirst(cursor.nowToken())) {
                        parseFuncRParams(); //funcR
                        checkToken(TokenType.RPARENT, new RParenLacked());
                    } else {
                        checkToken(TokenType.RPARENT, new RParenLacked());
                    }

                } else {
                    // ')'
                    checkToken(TokenType.RPARENT, new RParenLacked());
                }

            } else if (cursor.nowToken().getType() == TokenType.PLUS
                    || cursor.nowToken().getType() == TokenType.MINUS
                    || cursor.nowToken().getType() == TokenType.NOT) {
                if (node instanceof UnaryExp) {
                    ((UnaryExp) node).setUnaType("Una");
                }
                // | UnaryOp UnaryExp
                parseUnaryOp();
                parseUnaryExp();
            } else {
                if (node instanceof UnaryExp) {
                    ((UnaryExp) node).setUnaType("Prim");
                }
                parsePrimaryExp();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    public void parseUnaryOp() {
        try {
            Node node = astBuilder.startBld(TokenType.UnaryOp, cursor.atLine());
            parseTerm();
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }
    }

    //FuncRParams → Exp { ',' Exp }
    public void parseFuncRParams() throws ParserException {
        try {
            Node node = astBuilder.startBld(TokenType.FuncRParams, cursor.atLine());
            parseExp();
            while (cursor.nowToken().getType() == TokenType.COMMA) {
                parseTerm(); // pass ,
                parseExp();
            }
            astBuilder.endBld(node, cursor.prevTokenAtLine());
        } catch (EOFException e) {
            ;
        }

    }

    // UnaryExp { ('*' | '/' | '%') UnaryExp }
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    public void parseMulExp() throws ParserException {
        try {
//            Node node = astBuilder.startBld(TokenType.MulExp, cursor.atLine());
            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.MulExp, cursor.atLine(), posRecord);
            parseUnaryExp();
            astBuilder.endBld(node1, cursor.prevTokenAtLine());
            while (cursor.nowToken().getType() == TokenType.MULT
                    || cursor.nowToken().getType() == TokenType.DIV
                    || cursor.nowToken().getType() == TokenType.MOD) {
                parseTerm();
                parseUnaryExp();
                Node node = astBuilder.insertNode(TokenType.MulExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());

        } catch (EOFException e) {
            ;
        }

    }

    // AddExp → MulExp {('+' | '−') MulExp}
    public void parseAddExp() throws ParserException {
        try {

//            Node node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.AddExp, cursor.atLine(), posRecord);
            parseMulExp();
            astBuilder.endBld(node1, cursor.atLine());
            while (cursor.nowToken().getType() == TokenType.PLUS
                    || cursor.nowToken().getType() == TokenType.MINUS) {
                parseTerm();
                parseMulExp();
                Node node = astBuilder.insertNode(TokenType.AddExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());
        } catch (EOFException e) {
            ;
        }
    }

    //RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp}
    public void parseRelExp() throws ParserException {
        try {
//            Node node = astBuilder.startBld(TokenType.RelExp, cursor.atLine());

            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.RelExp, cursor.atLine(), posRecord);

            parseAddExp();
            astBuilder.endBld(node1, cursor.prevTokenAtLine());
            while (cursor.nowToken().getType() == TokenType.GEQ
                    || cursor.nowToken().getType() == TokenType.GRE
                    || cursor.nowToken().getType() == TokenType.LEQ
                    || cursor.nowToken().getType() == TokenType.LSS) {
                parseTerm();
                parseAddExp();
                Node node = astBuilder.insertNode(TokenType.RelExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());
        } catch (EOFException e) {
            ;
        }
    }

    // EqExp → RelExp { ('==' | '!=') RelExp}
    public void parseEqExp() throws ParserException {
        try {
//            Node node = astBuilder.startBld(TokenType.EqExp, cursor.atLine());

            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.EqExp, cursor.atLine(), posRecord);
            parseRelExp();
            astBuilder.endBld(node1, cursor.prevTokenAtLine());
            while (cursor.nowToken().getType() == TokenType.EQL
                    || cursor.nowToken().getType() == TokenType.NEQ) {
                parseTerm();
                parseRelExp();
                Node node = astBuilder.insertNode(TokenType.EqExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());
        } catch (EOFException e) {
            ;
        }
    }

    // LAndExp → EqExp { '&&' EqExp }
    public void parseLAndExp() throws ParserException {
        try {
//            Node node = astBuilder.startBld(TokenType.LAndExp, cursor.atLine());
            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.LAndExp, cursor.atLine(), posRecord);
            parseEqExp();
            astBuilder.endBld(node1, cursor.prevTokenAtLine());
            while (cursor.nowToken().getType() == TokenType.AND) {
                parseTerm();
                parseEqExp();
                Node node = astBuilder.insertNode(TokenType.LAndExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());
        } catch (EOFException e) {
            ;
        }
    }

    // LOrExp → LAndExp {'||' LAndExp}
    public void parseLOrExp() throws ParserException {
        try {
//            Node node = astBuilder.startBld(TokenType.LOrExp, cursor.atLine());
            int posRecord = astBuilder.posRecord();
            Node node1 = astBuilder.insertNode(TokenType.LOrExp, cursor.atLine(), posRecord);
            parseLAndExp();
            astBuilder.endBld(node1, cursor.prevTokenAtLine());
            while (cursor.nowToken().getType() == TokenType.OR) {
                parseTerm();
                parseLAndExp();
                Node node = astBuilder.insertNode(TokenType.LOrExp, cursor.atLine(), posRecord);
                astBuilder.endBld(node, cursor.prevTokenAtLine());
//                astBuilder.endBld(node, cursor.atLine());
//                node = astBuilder.startBld(TokenType.AddExp, cursor.atLine());
            }
//            astBuilder.endBld(node, cursor.atLine());
        } catch (EOFException e) {
            ;
        }
    }

    // ConstExp → AddExp 注：使用的Ident 必须是常量 // 存在即可
    public void parseConstExp() throws ParserException, EOFException {
        Node node = astBuilder.startBld(TokenType.ConstExp, cursor.atLine());
        parseAddExp();
        astBuilder.endBld(node, cursor.prevTokenAtLine());
    }
}
