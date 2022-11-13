package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Div;
import IRGen.IRs.Geq;
import IRGen.IRs.Gt;
import IRGen.IRs.Leq;
import IRGen.IRs.Lt;
import IRGen.IRs.Mod;
import IRGen.IRs.Mul;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class MulExp extends Node{
    public MulExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
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
        for (Node son : sons) {
            son.buildCFG(context, ret);
            if (son instanceof MulExp) {
                l = ret.res;
                ret.clearRes();
            } else if (son instanceof UnaryExp) {
                r = ret.res;
                ret.clearRes();
            } else if (son instanceof Token) {
                t = son.getType();
            }
        }
        if (sons.size() == 1) {
            ret.res = r;
        } else {
            int a = 0;
            int b = 0;

            try {
                a = Integer.parseInt(l.getName());
                b = Integer.parseInt(r.getName());
                // both are Number;

                switch (t) {
                    case MULT: // <=
                        res = new Var(String.valueOf(a * b));
                        break;
                    case DIV: // <
                        res = new Var(String.valueOf(a / b));
                        break;
                    case MOD: // >
                        res = new Var(String.valueOf(a % b));
                        break;

                    default:
                        break;
                }

//            res = String.valueOf(isPlus ? a + b : a - b);
            } catch (NumberFormatException e) {
                res = Var.makeIrTmpId();
                switch (t) {
                    case MULT: // <=
                        CFG.getInstance().insertIrToNowBBlock(new Mul(res, l, r));
                        break;
                    case DIV: // <

                        CFG.getInstance().insertIrToNowBBlock(new Div(res, l, r));
                        break;
                    case MOD: // >

                        CFG.getInstance().insertIrToNowBBlock(new Mod(res, l, r));
                        break;
                    default:
                        break;
                }
                // 需要保证的就是res已经算出，或者以tmp 的形式计算出来
            }
            ret.res = res;

        }

    }
}
