package Nodes;

import IRGen.CFG.BBlock;
import IRGen.CFG.CFG;
import IRGen.CFG.Func;
import IRGen.IRs.GetParam;
import IRGen.IRs.Return;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.IntFuncRetLacked;
import Parser.Error.RedefinedIdent;
import SymTab.Context;
import SymTab.Ret;
import SymTab.SymTab;
import SymTab.ErrorRecord;

import java.util.ArrayList;

public class MainFuncDef extends Node{
    public MainFuncDef(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    // 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    // 名字重定义 b
    // 有返回值的函数缺少return语句
    @Override
    public void checkError(Context context, Ret ret) {
        boolean isVoid = false;
        int identLine = 0;
        String name = null;
        ret = new Ret();
        for (Node son : sons) {
            // Stmt Need if inVoid to check Return (VoidButReturn)

            if (son instanceof Token && son.getType() == TokenType.LPARENT) {
                SymTab.getInstance().beginScope();
            }
            if (son instanceof Block) {
                context.inVoid = 0;
                // IntFuncButNoReturn

            }
            son.checkError(context, ret);

            if (son instanceof Block) {
                if (ret.hasRet == 0 && !isVoid) {
                    ErrorRecord.getInstance().recordError(new IntFuncRetLacked(lineNum2));
                }
                ret.clearHasRet();
            }
            if (son instanceof Token && son.getType() == TokenType.MAIN) {
                name = ((Token) son).getContent();
                identLine = son.getLineNum1();
            }

        }
        SymTab.getInstance().endScope();
        if (!SymTab.getInstance().defFunc(name, ret.dims, isVoid)) {
            ErrorRecord.getInstance().recordError(new RedefinedIdent(identLine));
        }
        context.inVoid = 0;
        ret.hasRet = 0;
    }


    @Override
    public void buildCFG(Context context, Ret ret) {

        boolean isVoid = false;
        String name = null;
        ret = new Ret();
        ArrayList<Var> args = new ArrayList<>();
        for (Node son : sons) {
            // Stmt Need if inVoid to check Return (VoidButReturn)

            if (son instanceof Token && son.getType() == TokenType.LPARENT) {
                SymTab.getInstance().beginScope();
            }

            if (son instanceof Block) {

                context.inVoid = isVoid ? 1 : 0;

                SymTab.getInstance().defFuncPrevScope(name, null, isVoid);
                String funcIrId = Var.makeIrFuncId(name);
                assert args != null;
                CFG.getInstance().setNowFunc(new Func(funcIrId, args.size()));
                CFG.getInstance().setNowBBlock(new BBlock("MainFuncBlock")); // funcBlock
                // in FuncBlock First get its param
                CFG cfg = CFG.getInstance();
                for (int i = 0; i < args.size(); i++) {
                    cfg.insertIrToNowBBlock(new GetParam(args.get(i), i));
                }

            }

            son.buildCFG(context, ret);
            if (son instanceof FuncType) {
                isVoid = ret.funcTypeVoid > 0;
                ret.clearFuncTypeVoid();
            }
            if (son instanceof Block) {
                CFG.getInstance().insertIrToNowBBlock(new Return(new Var("void")));
                ret.clearHasRet();
            }

            if (son instanceof FuncFParams) {
                args = new ArrayList<>(ret.args);
                ret.clearArgs();
            }

            if (son instanceof Token && son.getType() == TokenType.MAIN) {
                name = ((Token) son).getContent();
            }

        }
        SymTab.getInstance().endScope();

        context.inVoid = 0;
        ret.hasRet = 0;
    }
}
