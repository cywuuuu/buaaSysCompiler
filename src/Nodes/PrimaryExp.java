package Nodes;

import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;
//PrimaryExp → '(' Exp ')' | LVal | Number
public class PrimaryExp extends Node{
    public PrimaryExp(int lineNum1, int lineNum2, TokenType type) {
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
        }
    }
}
