package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Malloc;
import IRGen.IRs.Move;
import IRGen.IRs.Sw;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.RedefinedIdent;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymTab;
import SymTab.ErrorRecord;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConstDef extends Node {
    public ConstDef(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    @Override
    public void checkError(Context context, Ret ret) {

        ArrayList<Integer> arraySizes = new ArrayList<>();
        String name = "";
        int identLine = 0;
        for (Node son : sons) {
            son.checkError(context, ret);
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                name = ((Token) son).getContent();
                identLine = son.lineNum2;
            }
            if (son instanceof ConstExp) {
                arraySizes.add(ret.val);
                ret.clearVal();
            }
        }

        if (!SymTab.getInstance().defVar(name, arraySizes, null, true)) {
            ErrorRecord.getInstance().recordError(new RedefinedIdent(identLine));
        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        ArrayList<Integer> arraySizes = new ArrayList<>();
        ArrayList<Integer> initVal = new ArrayList<>();
        ArrayList<Var> initStr = new ArrayList<>();
        String name = "";

        for (Node son : sons) {
            son.buildCFG(context, ret);
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                name = ((Token) son).getContent();
            } else if (son instanceof ConstExp) {
                arraySizes.add(Integer.parseInt(ret.res.getName()));
            } else if (son instanceof ConstInitVal) {
                initVal.addAll(ret.initStr.stream().map(o -> {
                    return Integer.parseInt(o.getName());
                }).collect(Collectors.toList()));


            }

        }

        Var id = SymTab.getInstance().defVarWithIRId(name, arraySizes, initVal, true);


    }
}
