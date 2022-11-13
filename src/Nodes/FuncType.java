package Nodes;

import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class FuncType extends Node{
    public FuncType(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    public void checkError(Context context, Ret ret) {
        for (Node node : sons) {
            node.checkError(context, ret);
            if (node instanceof Token && node.getType() == TokenType.VOID) {
                ret.funcTypeVoid = 1;
            }
        }

    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        for (Node son :sons) {
            son.buildCFG(context, ret);
        }
    }
}
