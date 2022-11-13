package Nodes;

import Lexer.TokenType;
import SymTab.Context;
import SymTab.ErrorRecord;
import SymTab.Ret;

public class ConstDecl extends Node {
    public ConstDecl(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    @Override
    public void checkError(Context context, Ret ret) {

        for (Node son : sons) {
            son.checkError(context, ret);
        }

    }

    @Override
    public void buildCFG(Context context, Ret ret) {

        for (Node node : sons) {
            node.buildCFG(context, ret);
        }

    }
}
