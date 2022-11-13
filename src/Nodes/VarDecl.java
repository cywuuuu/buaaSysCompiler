package Nodes;

import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class VarDecl extends Node{
    public VarDecl(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    @Override
    public void checkError(Context context, Ret ret) {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        for (Node son : sons) {
            son.checkError(context, ret);
        }

    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        for (Node son:sons) {
            son.buildCFG(context, ret);
        }
    }
}
