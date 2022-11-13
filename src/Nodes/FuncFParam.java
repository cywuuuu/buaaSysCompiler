package Nodes;

import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.RedefinedIdent;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymTab;
import SymTab.ErrorRecord;

import java.util.ArrayList;

public class FuncFParam extends Node {
    public FuncFParam(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    //FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    @Override
    public void checkError(Context context, Ret ret) {
        boolean brackOn = false;
        String name = null;
        int val = 0;
        ArrayList<Integer> varSizes = new ArrayList<>();
        int identLine = 0;
        for (Node son : sons) {
            son.checkError(context, ret);

            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                identLine = son.lineNum2;
                name = ((Token) son).getContent();
            }


            if (son instanceof ConstExp) {
                // ret.val 就是 constExp 's result
                varSizes.set(varSizes.size() - 1, ret.val);

                ret.clearVal();
            }
            if (son.getType() == TokenType.RBRACK) {

            }
            if (son.getType() == TokenType.LBRACK) {
                varSizes.add(0);

            }
        }
        ret.vardim = varSizes.size();
        if (!SymTab.getInstance().defVar(name, varSizes, null, false)) {
            ErrorRecord.getInstance().recordError(new RedefinedIdent(identLine));
        }

    }

    //FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    @Override
    public void buildCFG(Context context, Ret ret) {
        String name = null;
        int val = 0;
        ArrayList<Integer> varSizes = new ArrayList<>(); // 获取实际大小确保数组？
        for (Node node : sons) {
            node.buildCFG(context, ret);
            if (node instanceof Token && node.getType() == TokenType.IDENT) {
                name = ((Token) node).getContent();
            }
            if (node instanceof ConstExp) { // (int aaa[][3],
                int a = 0;
                try {
                    a = Integer.parseInt(ret.res.getName());

                } catch (NumberFormatException e) {

                }
                varSizes.set(varSizes.size() - 1, a);
                ret.clearRes();
            }
            if (node.getType() == TokenType.LBRACK) {
                varSizes.add(0);
            }
        }
        ret.vardim = varSizes.size();
        ret.param = SymTab.getInstance().defVarWithIRId(name, varSizes, null, false);
    }
}
