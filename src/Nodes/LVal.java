package Nodes;

import IRGen.CFG.CFG;
import IRGen.IRs.Add;
import IRGen.IRs.GetAddr;
import IRGen.IRs.Lw;
import IRGen.IRs.Move;
import IRGen.IRs.Mul;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.UndefinedIdent;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymTab;
import SymTab.SymVar;
import SymTab.ErrorRecord;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LVal extends Node{
    public LVal(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }
    // LVal → Ident {'[' Exp ']'}
    // 不能改变常量的值
    @Override
    public void checkError(Context context, Ret ret) {
        int identLine = 0;
        SymVar var = null;
        int remainVarDim = 0;
        boolean varConst = false;
        for (Node son : sons) {
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                var = SymTab.getInstance().getVar(((Token) son).getContent());
                identLine = son.lineNum2;

                if (var == null) {
                    ErrorRecord.getInstance().recordError(new UndefinedIdent(son.lineNum2));
                }
                if (var != null && var.isConstVar()) {
                    varConst = true;
                } else {
                    varConst = false;
                }
                if (var != null) {
                    remainVarDim = var.getVarDim();
                }

            }
            if (son instanceof Exp) {
                remainVarDim -= 1;
            }
            son.checkError(context, ret);
        }
        if (var != null) {
            ret.vardim = remainVarDim;
        }
        ret.hasConst = varConst ? 1 : 0;

    }
    // LVal → Ident {'[' Exp ']'}
    @Override
    public void buildCFG(Context context, Ret ret) {
        SymVar lval = null;
        String name = "";
        boolean inrealLval = context.inLVal > 0;
        context.inLVal = 0;
        ArrayList<Var> arrPos = new ArrayList<>();
        boolean posCalculable = true;
        for (Node son :sons) {
            son.buildCFG(context, ret);
            if (son instanceof Token && son.getType() == TokenType.IDENT) {
                name = ((Token) son).getContent();
            }
            if (son instanceof Exp) {
                arrPos.add(ret.res);
                try {
                    Integer.parseInt(ret.res.getName());
                } catch (NumberFormatException e) {
                    posCalculable = false;
                }
            }

        }
        lval = SymTab.getInstance().getVar(name);
        Var offs = null;
        boolean isArray = false;
        if (posCalculable && (lval.isConstVar()) || SymTab.getInstance().isGlob()) {
            // 确定了内容和位置（Const Var）都是int直接求了得了, global 内容可变不在这里
            ArrayList<Integer> intPos = new ArrayList<>();
            intPos = (ArrayList<Integer>) arrPos.stream().map(o -> Integer.parseInt(o.getName())).collect(Collectors.toList());
            ret.res = new Var(String.valueOf(lval.getArrVal(intPos))); // result of Lval  a[1][1]
            return;
        } else {
            // cal offset from ircode
            if (!arrPos.isEmpty()) { // 数组
                Var base = Var.makeIrTmpId();
                Var offset = Var.makeIrTmpId();
                CFG.getInstance().insertIrToNowBBlock(new Move(base, new Var("1")));
                CFG.getInstance().insertIrToNowBBlock(new Move(offset, new Var("0")));
                ArrayList<Integer> arrDim = lval.getVarSizes();
                for (int i = arrDim.size() - 1; i >= arrPos.size(); i--) {
                    CFG.getInstance().insertIrToNowBBlock(new Mul(base, base, new Var(String.valueOf(arrDim.get(i)))));
                }

                Var tmp = Var.makeIrTmpId();
                for (int i = arrPos.size() - 1; i >= 0; i--) {

                    CFG.getInstance().insertIrToNowBBlock(new Mul(tmp, base, arrPos.get(i)));
                    CFG.getInstance().insertIrToNowBBlock(new Add(offset, offset, tmp));
                    CFG.getInstance().insertIrToNowBBlock(new Mul(base, base, new Var(String.valueOf(arrDim.get(i)))));
                }
                offs = offset;
                isArray = true;
            } else if (lval.isGlobVar()) {
                // 全局变量看做数组， 其pos = 0
                offs = new Var("0");
                isArray = true;
            }

            if (inrealLval) {
                ret.isArray = isArray ? 1 : 0;
                ret.res = lval.varId; // might deepcopy
                ret.param = offs;
            } else {
                if (arrPos.isEmpty() && !lval.isGlobVar()) {
                    // 局部变量(非数组)在右 取出来
                    ret.res = lval.varId;
                } else {
                    // 全局、常量、数组 通过load取出来
                    Var res = Var.makeIrTmpId();
                    if (arrPos.size() == lval.getVarDim()) {
                        CFG.getInstance().insertIrToNowBBlock(new Lw(res, lval.varId, offs));
                    } else {
                        CFG.getInstance().insertIrToNowBBlock(new GetAddr(res, lval.varId, offs));
                    }
                    ret.res = res;
                }
            }




        }





    }
}
