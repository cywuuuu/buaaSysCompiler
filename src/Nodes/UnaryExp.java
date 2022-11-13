package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Call;
import IRGen.IRs.GetRet;
import IRGen.IRs.IR;
import IRGen.IRs.Neg;
import IRGen.IRs.Not;
import IRGen.IRs.SetParam;
import IRGen.IRs.Sub;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.ParserException;
import Parser.Error.UndefinedError;
import Parser.Error.UndefinedIdent;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymFunc;
import SymTab.SymTab;
import SymTab.ErrorRecord;

import javax.naming.Name;
import java.util.ArrayList;

public class UnaryExp extends Node {
    public UnaryExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    private String UnaType = null;

    public String getUnaType() {
        return UnaType;
    }

    public void setUnaType(String unaType) {
        UnaType = unaType;
    }

    //函数参数类型不匹配
    //函数参数个数不匹配
    //未定义的名字
    // Ident '(' [FuncRParams] ')' | PrimaryExp | UnaryOp UnaryExp
    // ret vardim
    @Override
    public void checkError(Context context, Ret ret) {
        int identLine = 0;
        SymFunc func = null;
        ret.clearDims();
        int vardim = 0;
        boolean checkedParam = false;

        for (Node son : sons) {
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                func = SymTab.getInstance().getFunc(((Token) son).getContent());
                identLine = son.lineNum2;
                if (func == null) {
                    ErrorRecord.getInstance().recordError(new UndefinedIdent(son.lineNum2));
                }
            }
            son.checkError(context, ret);
        }

        if (func != null) {
            ParserException e = func.checkParams(ret.dims); // func(int[3][2], int[3], int, int)
            ret.clearDims();

            if (e != null) {
                e.setLine(identLine);
                ErrorRecord.getInstance().recordError(e);
            }
        }

        if (func != null && func.getVoidFunc()) {
            ret.vardim = -1;
        } else if (func != null && !func.getVoidFunc()) {
            ret.vardim = 0;
        }
    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        SymFunc func = null;
        String name = "";
        ArrayList<Var> args = new ArrayList<>();
        TokenType op = TokenType.PLUS;
        if (UnaType.equals("Call")) {
            for (Node son : sons) {
                son.buildCFG(context, ret);
                if (son instanceof Token && son.getType() == TokenType.IDENT) {
                    name = ((Token) son).getContent();
                    func = SymTab.getInstance().getFunc(((Token) son).getContent());
                }

                if (son instanceof FuncRParams) {
                    args = new ArrayList<>(ret.args);
                    ret.clearArgs();
                }


            }
            assert func != null;

            String funcIR = Var.makeIrFuncId(name);
            int i = 0;
            for (Var arg : args) {
                CFG.getInstance().insertIrToNowBBlock(new SetParam(arg, i));
                i++;
            }

            CFG.getInstance().insertIrToNowBBlock(new Call(new Var(funcIR)));
            Var res = null;
            if (func.getVoidFunc()) {
                res = new Var("void");

            } else {
                res = Var.makeIrTmpId();
                CFG.getInstance().insertIrToNowBBlock(new GetRet(res));
            }
            ret.res = res;
        } else if (UnaType.equals("Prim")) {
            for (Node son : sons) {
                son.buildCFG(context, ret);
            }
        } else { // UnaryOp UnaryExp
            for (Node son : sons) {

                son.buildCFG(context, ret);
                if (son instanceof UnaryOp) {
                    op = ret.op;
                    ret.clearOp();
                }

                if (son instanceof UnaryExp) {
                    try {
                        int a = Integer.parseInt(ret.res.getName());

                        if (op == TokenType.MINUS) {
                            int res = -a;
                            ret.res = new Var(String.valueOf(res));
                        } else if (op == TokenType.NOT) {
                            // v非零的时候值为0，否则值为1
                            int res = (a != 0) ? 0 : 1;
                            ret.res = new Var(String.valueOf(res));
                        }
                    } catch (NumberFormatException e) {

                        if (op == TokenType.MINUS) {
                            Var res = Var.makeIrTmpId();
                            CFG.getInstance().insertIrToNowBBlock(new Neg(res, ret.res));
                            ret.res = res;
                        } else if (op == TokenType.NOT) {
                            // v非零的时候值为0，否则值为1
                            Var res = Var.makeIrTmpId();
                            CFG.getInstance().insertIrToNowBBlock(new Not(res, ret.res));
                            ret.res = res;
                        }
                    }
                }
            }

        }

    }
}
