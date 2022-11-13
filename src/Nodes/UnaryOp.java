package Nodes;

import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.ParserException;
import Parser.Error.UndefinedError;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymFunc;
import SymTab.SymTab;

public class UnaryOp extends Node{
    public UnaryOp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    @Override
    public void checkError(Context context, Ret ret) {
        for (Node son : sons) {
            son.checkError(context, ret);

        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        for (Node son :sons) {
            son.buildCFG(context, ret);
            if (son instanceof Token) {
                ret.op = son.getType();
            }
        }
    }
}
