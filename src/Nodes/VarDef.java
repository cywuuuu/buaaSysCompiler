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

public class VarDef extends Node{
    public VarDef(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // VarDef → Ident { '[' ConstExp ']' } // 包含普通变量、一维数组、二维数组定义 | Ident { '[' ConstExp ']' } '=' InitVal
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

        if (!SymTab.getInstance().defVar(name, arraySizes, null, false)) {
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
        for (Node son:sons) {
            son.buildCFG(context, ret);
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                name = ((Token) son).getContent();
            }

            else if (son instanceof ConstExp) {
                arraySizes.add(Integer.parseInt(ret.res.getName()));
            } else if (son instanceof InitVal) {
                if (!SymTab.getInstance().isGlob()) {
                    initStr = ret.initStr;
                } else {
                    initVal.addAll(ret.initStr.stream().map(o -> Integer.parseInt(o.getName())).collect(Collectors.toList()));
                }

            }

        }

        Var id = SymTab.getInstance().defVarWithIRId(name, arraySizes, initVal, false);
        if (!SymTab.getInstance().isGlob()) { // glob/const was created in data, non glob non const in stack

            if (arraySizes.isEmpty()) { // def var like x e.g. int c = 1;
                if (!initStr.isEmpty()) { // int c = 1;
                    // str
                    CFG.getInstance().insertIrToNowBBlock(new Move(id, initStr.get(0)));
                } else { // int c; // default value is 0
                    CFG.getInstance().insertIrToNowBBlock(new Move(id, new Var("0"))) ; //default value
                }

            } else {
                // int c[2][4] = {{1, 2}, {2, 4}};
                int intNum = arraySizes.stream().reduce(1, (acc, n) -> acc * n);// 2 * 4
                Var arr = id;
                CFG.getInstance().insertIrToNowBBlock(new Malloc(arr, intNum));
                int tmppos = 0;
                for (Var i : initStr) {
                    CFG.getInstance().insertIrToNowBBlock(new Sw(i, arr, new Var(String.valueOf(tmppos++))));
                }
            }
        }


    }
}
