package Nodes;

import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class Number extends Node{
    public Number(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    @Override
    public void checkError(Context context, Ret ret) {

        for (Node son : sons) {
            son.checkError(context, ret);
            if (son instanceof Token && son.getType() == TokenType.INT_CONST) {
                ret.val = new Integer(((Token) son).getContent());
                ret.vardim = 0;
            }
        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        for (Node son :sons) {
            son.buildCFG(context, ret);
            if (son instanceof Token && son.getType() == TokenType.INT_CONST) {
                ret.res = new Var(((Token) son).getContent());
                ret.val = new Integer(((Token) son).getContent());
                ret.vardim = 0;
            }
        }
    }
}
