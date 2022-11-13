package Nodes;

import IRGen.IRs.Var;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

import java.util.ArrayList;

public class FuncRParams extends Node{
    public FuncRParams(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    //FuncRParams → Exp { ',' Exp }
    @Override
    public void checkError(Context context, Ret ret) {
        ArrayList<Integer> dims = new ArrayList<>();
        for (Node son : sons) {
            son.checkError(context, ret);
            if (son instanceof Exp) {
                dims.add(ret.vardim);
                ret.clearDims();
            }
        }
        ret.dims = dims;
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        ArrayList<Var> args = new ArrayList<>();
        for (Node node : sons) {
            node.buildCFG(context, ret);
            if (node instanceof Exp) {
                args.add(ret.res);
                ret.clearRes();
            }
        }
        ret.args = args;
    }

}
