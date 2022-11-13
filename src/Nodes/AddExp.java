package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Add;
import IRGen.IRs.Sub;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class AddExp extends Node {

    public AddExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    // Done ErrCheck
    @Override
    public void checkError(Context context, Ret ret) {

        for (Node node : sons) {
            node.checkError(context, ret);

        }
    }

    // AddExp → MulExp | AddExp ('+' | '−') MulExp
    @Override
    public void buildCFG(Context context, Ret ret) {
        boolean isPlus = true;
        Var res = null;
        Var l = null;
        Var r = null;
        for (Node son : sons) {
            son.buildCFG(context, ret);
            if (son instanceof AddExp) {
                l = ret.res;
                ret.clearRes();
            } else if (son instanceof MulExp) {
                r = ret.res;
                ret.clearRes();
            } else if (son instanceof Token && son.getType() == TokenType.MINUS) {
                isPlus = false;
            }
        }
        if (sons.size() == 1) {
            ret.res = r;
        } else {
            int a = 0;
            int b = 0;

            try {
                assert l != null;
                a = Integer.parseInt(l.getName());
                assert r != null;
                b = Integer.parseInt(r.getName());
                // both are Number;
                res = new Var(String.valueOf(isPlus ? a + b : a - b));
            } catch (NumberFormatException e) {
                res = Var.makeIrTmpId();
                CFG.getInstance().insertIrToNowBBlock(isPlus ? new Add(res, l, r)
                        : new Sub(res, l, r));
            }
            ret.res = res;
        }


    }
}
