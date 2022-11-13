package Nodes;

import IRGen.IRs.Var;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;


import java.util.ArrayList;

public class ConstInitVal extends Node{
    public ConstInitVal(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // ConstInitVal → ConstExp    //get res -> initStr
    //              | '{' [ ConstInitVal { ',' ConstInitVal } ] '}' // initStr addall
    // return initStr<>
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
            if (son instanceof ConstExp) {
                initStr.add(ret.res);
            } else if (son instanceof ConstInitVal) {
                initStr.addAll(ret.initStr);
            }
        }
        ret.initStr = initStr;
    }
}
