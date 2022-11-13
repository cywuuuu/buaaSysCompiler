package Nodes;

import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class ConstExp extends Node{
    public ConstExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    // Done ErrCheck
    @Override
    public void checkError(Context context, Ret ret) {


        for (Node node : sons) {
            node.checkError(context, ret);

        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        for (Node son :sons) {
            son.buildCFG(context, ret);
        }
    }
}
