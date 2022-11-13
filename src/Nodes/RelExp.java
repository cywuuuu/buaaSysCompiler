package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Add;
import IRGen.IRs.Geq;
import IRGen.IRs.Gt;
import IRGen.IRs.Leq;
import IRGen.IRs.Lt;
import IRGen.IRs.Sub;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class RelExp extends Node{
    public RelExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    @Override
    public void checkError(Context context, Ret ret) {

        for (Node son : sons) {
            son.checkError(context, ret);

        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        TokenType t = null;
        Var res = null;
        Var l = null;
        Var r = null;
        if (sons.size() >= 3) {
            for (Node son : sons) {
                son.buildCFG(context, ret);
                if (son instanceof RelExp) {
                    l = ret.res;
                    ret.clearRes();
                } else if (son instanceof AddExp) {
                    r = ret.res;
                    ret.clearRes();
                } else if (son instanceof Token) {
                    t = son.getType();
                }
            }

            int a = 0;
            int b = 0;

            try {
                a = Integer.parseInt(l.getName());
                b = Integer.parseInt(r.getName());
                // both are Number;

                switch (t) {
                    case LEQ: // <=
                        res = new Var(String.valueOf(a <= b ? 1 : 0));
                        break;
                    case LSS: // <
                        res = new Var(String.valueOf(a < b ? 1 : 0));
                        break;
                    case GRE: // >
                        res = new Var(String.valueOf(a > b ? 1 : 0));
                        break;
                    case GEQ: // >=
                        res = new Var(String.valueOf(a >= b ? 1 : 0));
                        break;
                    default:
                        break;
                }

//            res = String.valueOf(isPlus ? a + b : a - b);
            } catch (NumberFormatException e) {
                res = Var.makeIrTmpId();
                switch (t) {
                    case LEQ: // <=
                        CFG.getInstance().insertIrToNowBBlock(new Leq(res, l, r));
                        break;
                    case LSS: // <
                        CFG.getInstance().insertIrToNowBBlock(new Lt(res, l, r));
                        break;
                    case GRE: // >
                        CFG.getInstance().insertIrToNowBBlock(new Gt(res, l, r));
                        break;
                    case GEQ: // >=
                        CFG.getInstance().insertIrToNowBBlock(new Geq(res, l, r));
                        break;
                    default:
                        break;
                }
                // 需要保证的就是res已经算出，或者以tmp 的形式计算出来
            }
            ret.res = res;
        } else if (!sons.isEmpty()) {
            sons.get(0).buildCFG(context, ret);
        }


    }
}
