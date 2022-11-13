package Nodes;

import IRGen.IRs.Var;
import Lexer.TokenType;
import SymTab.*;
// CompUnit → {Decl} {FuncDef} MainFuncDef

public class CompUnit extends Node {
    public CompUnit(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    @Override
    public void checkError(Context context, Ret ret) {
        SymTab.getInstance().beginScope();
        for (Node node : sons) {
            node.checkError(context, ret);
        }
        SymTab.getInstance().endScope();
        SymTab.getInstance().clearAll();
        Var.clearCnt();
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        SymTab.getInstance().beginScope();

        for (Node node : sons) {
            node.buildCFG(context, ret);
        }

        SymTab.getInstance().endScope();

    }


}
