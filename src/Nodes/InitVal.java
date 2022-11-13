package Nodes;

import IRGen.IRs.Var;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

import java.util.ArrayList;

public class InitVal extends Node{
    public InitVal(int lineNum1, int lineNum2, TokenType type) {
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
        ArrayList<Var> initStr = new ArrayList<>();
        for (Node son : sons) {
            son.buildCFG(context, ret);
            if (son instanceof Exp) {
                initStr.add(ret.res);
            } else if (son instanceof InitVal) {
                initStr.addAll(ret.initStr);
            }
        }
        ret.initStr = initStr;
    }
}
