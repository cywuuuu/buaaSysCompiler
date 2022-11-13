package Nodes;

import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class Cond extends Node{
    public Cond(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // Done CheckErr
    @Override
    public void checkError(Context context, Ret ret) {


        for (Node node : sons) {
            node.checkError(context, ret);

        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {

        for (Node node : sons) {
            node.buildCFG(context, ret);
        }
    }
}
