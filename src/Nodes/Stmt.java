package Nodes;

import IRGen.CFG.BBlock;
import IRGen.CFG.CFG;
import IRGen.IRs.GetInt;
import IRGen.IRs.Jmp;
import IRGen.IRs.JmpFalse;
import IRGen.IRs.JmpTrue;
import IRGen.IRs.Move;
import IRGen.IRs.PrintInt;
import IRGen.IRs.PrintStr;
import IRGen.IRs.ReadInt;
import IRGen.IRs.Return;
import IRGen.IRs.Sw;
import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Error.BreakContinueOutLoop;
import Parser.Error.ConstantAssigned;
import Parser.Error.FmtCharUnmatched;
import Parser.Error.InvalidChar;
import Parser.Error.VoidFuncRetInt;
import SymTab.Context;
import SymTab.Ret;
import SymTab.ErrorRecord;
import SymTab.SymTab;

import java.util.ArrayList;

public class Stmt extends Node {

    public Stmt(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    private String stmtType = null;

    public String getStmtType() {
        return stmtType;
    }

    public void setStmtType(String stmtType) {
        this.stmtType = stmtType;
    }

    @Override
    public void checkError(Context context, Ret ret) {
        if (stmtType.equals("Assign")) {
            for (Node son : sons) {
                if (son instanceof LVal) {

                    son.checkError(context, ret);
                    if (ret.hasConst > 0) {
                        ErrorRecord.getInstance().recordError(new ConstantAssigned(son.getLineNum1()));
                    }
                    ret.clearHasConst();
                } else {
                    son.checkError(context, ret);
                }
            }

        } else if (stmtType.equals("Expr")) {
            for (Node son : sons) {
                son.checkError(context, ret);
            }
        } else if (stmtType.equals("If")) {
            for (Node son : sons) {
                son.checkError(context, ret);
            }
        } else if (stmtType.equals("While")) {
            for (Node son : sons) {
                if (son instanceof Stmt) {
                    context.inLoop += 1;
                    son.checkError(context, ret);
                    context.inLoop -= 1;
                } else {
                    son.checkError(context, ret);
                }

            }
        } else if (stmtType.equals("Break")) {
            for (Node son : sons) {
                if (son instanceof Token && son.getType() == TokenType.BREAK) {
                    if (context.inLoop == 0)
                        ErrorRecord.getInstance().recordError(new BreakContinueOutLoop(son.getLineNum1()));
                    son.checkError(context, ret);

                } else {
                    son.checkError(context, ret);
                }

            }

        } else if (stmtType.equals("Continue")) {
            for (Node son : sons) {
                if (son instanceof Token && son.getType() == TokenType.CONTINUE) {
                    if (context.inLoop == 0)
                        ErrorRecord.getInstance().recordError(new BreakContinueOutLoop(son.getLineNum1()));
                    son.checkError(context, ret);

                } else {
                    son.checkError(context, ret);
                }

            }
        } else if (stmtType.equals("Return")) {
            int retline = 0;
            for (Node son : sons) {
                if (son instanceof Token && son.getType() == TokenType.RETURN) {
                    retline = son.getLineNum1();
                    son.checkError(context, ret);

                } else if (son instanceof Exp) {

                    if (context.inVoid > 0)
                        ErrorRecord.getInstance().recordError(new VoidFuncRetInt(retline));
                    son.checkError(context, ret);

                } else {
                    son.checkError(context, ret);
                }

            }
            ret.hasRet = 1;
        } else if (stmtType.equals("Printf")) {
            int expCnt = 0;
            String fmtStr = "";
            int printLine = 0;
            int fmtStrLine = 0;
            for (Node son : sons) {
                son.checkError(context, ret);
                if (son instanceof Exp) {
                    expCnt += 1;

                } else if (son instanceof Token && son.getType() == TokenType.FMT_STR) {
                    fmtStr = ((Token) son).getContent();
                    fmtStrLine = son.lineNum2;
                } else if (son instanceof Token && son.getType() == TokenType.PRINTF) {
                    printLine = son.lineNum1;
                }
            }
            int fmtCnt = 0;
            boolean fmtError = false;
            for (int i = 1; i < fmtStr.length() - 2; i++) {
                char nowchar = fmtStr.charAt(i);
                char nextchar = fmtStr.charAt(i + 1);
                if (normalChar(nowchar, nextchar) || fmtChar(nowchar, nextchar)) {
                    if (fmtChar(nowchar, nextchar)) {
                        fmtCnt += 1;
                    }
                } else {
                    fmtError = true;
                }
            }
            if (fmtStr.charAt(0) == '\"' && fmtStr.charAt(fmtStr.length() - 1) == '\"') {
                ;
            } else {
                fmtError = true;
            }

            if (normalLastChar(fmtStr.charAt(fmtStr.length() - 2))) {
                ;
            } else {
                fmtError = true;
            }
            if (fmtError) {
                ErrorRecord.getInstance().recordError(new InvalidChar(fmtStrLine));
            }
            if (expCnt != fmtCnt) {
                ErrorRecord.getInstance().recordError(new FmtCharUnmatched(printLine));
            }

        } else if (stmtType.equals("Block")) {
            SymTab.getInstance().beginScope();
            for (Node son : sons) {
                son.checkError(context, ret);
            }
            SymTab.getInstance().endScope();
        } else {
            ;
        }

    }

    @Override
    public void buildCFG(Context context, Ret ret) {
        BBlock saveContinueRejudgeBlock = context.continueRejudgeB != null ? context.continueRejudgeB.deepCopy() : null;
        BBlock now = CFG.getInstance().getNowBBlock();
        int StmtsInLoop = context.inLoop;
        int saveJ2Else = context.j2Else;
        context.inLoop = 0;
        context.j2Else = 0;
        if (StmtsInLoop != 0) {
            //  continue跳入第一句，所以单开block
            context.continueRejudgeB = new BBlock("rejudgeAfterHitContinue");
        }


        if (stmtType.equals("Assign")) {
            // LVal '=' Exp ';'
            boolean assigningArray = false;
            Var lval = null;
            Var expVal = null;
            Var offset = null;
            for (Node node : sons) {
                if (node instanceof LVal) {
                    context.inLVal = 1;
                }
                node.buildCFG(context, ret);
                // 左部
                if (node instanceof LVal) {
                    context.inLVal = 0;
                    assigningArray = ret.isArray != 0;
                    ret.clearIsArray();
                    lval = ret.res;
                    ret.clearRes();
                    offset = ret.param;
                    ret.clearParam();
                }

                // 右部
                if (node instanceof Token && node.getType() == TokenType.GETINT) {
                    expVal = Var.makeIrTmpId();
                    CFG.getInstance().insertIrToNowBBlock(new ReadInt());
                    CFG.getInstance().insertIrToNowBBlock(new GetInt(expVal));
                }
                if (node instanceof Exp) {
                    expVal = ret.res;
                    ret.clearRes();
                }
            }

            if (assigningArray) { // a[1] = 3
                CFG.getInstance().insertIrToNowBBlock(new Sw(expVal, lval, offset));
            } else {
                CFG.getInstance().insertIrToNowBBlock(new Move(lval, expVal));
            }
        } else if (stmtType.equals("Expr")) {
            for (Node son : sons) {
                son.buildCFG(context, ret);
            }
        } else if (stmtType.equals("If")) {
            //'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.无else
            BBlock saveEndB = context.endBlock != null ? context.endBlock.deepCopy() : null;
            int savej2Else = context.j2Else;
            boolean hasElse = false;

            for (Node son : sons) {
                if (son instanceof Token && son.getType() == TokenType.ELSE) {
                    hasElse = true;
                    break;
                }
            }
            sons.get(2).buildCFG(context, ret);

            if (hasElse) {
                BBlock elseBlock = new BBlock("elseStmt");
                BBlock ifBlock = new BBlock("ifStmt");
                BBlock endBlock = new BBlock("ifEnd");
                context.endBlock = endBlock;
                CFG.getInstance().insertIrToNowBBlock(new JmpFalse(elseBlock, ret.res));

                // cond 没寄 顺势去if
                context.j2Else = 1; // StmtIf 记得 转去end
                CFG.getInstance().setNowBBlock(ifBlock);
                sons.get(4).buildCFG(context, ret);

                // cond 寄了， 那么就去else
                // 下面的代码 就是else了， 切换到else
                CFG.getInstance().setNowBBlock(elseBlock);
                context.j2Else = 0;
                sons.get(6).buildCFG(context, ret);

                // 然后进入end
                CFG.getInstance().setNowBBlock(endBlock);

            } else {
                BBlock ifBlock = new BBlock("ifStmt");
                BBlock endBlock = new BBlock("ifEnd");
                CFG.getInstance().insertIrToNowBBlock(new JmpFalse(endBlock, ret.res));
                // cond 没寄 落下来
                CFG.getInstance().setNowBBlock(ifBlock);
                sons.get(4).buildCFG(context, ret);
                context.j2Else = 0;

                // cond 寄了
                CFG.getInstance().setNowBBlock(endBlock);

            }
            context.j2Else = savej2Else;
            context.endBlock = saveEndB;
        } else if (stmtType.equals("While")) {
            // 'while' '(' Cond ')' Stmt
            BBlock saveBreak = context.breakBlock != null ? context.breakBlock.deepCopy() : null;
            int inLoop = context.inLoop;
            BBlock endBlock = new BBlock("whileEnd");
            BBlock whileBlock = new BBlock("whileLoopStmt");
            sons.get(2).buildCFG(context, ret);// cond check
            Cond saveCond = (Cond) context.rejudgeCond != null ? (Cond) context.rejudgeCond.deepCopy() : null;
            CFG.getInstance().insertIrToNowBBlock(new JmpFalse(endBlock, ret.res));

            // cond ok 进whileBlock
            CFG.getInstance().setNowBBlock(whileBlock);
            context.breakBlock = endBlock;
            context.inLoop = 1;
            context.rejudgeCond = sons.get(2);
            sons.get(4).buildCFG(context, ret);

            // 复原context,
            context.breakBlock = saveBreak;
            context.rejudgeCond = saveCond;
            context.inLoop = inLoop;
            // 寄了,
            CFG.getInstance().setNowBBlock(endBlock);

        } else if (stmtType.equals("Break")) {
            CFG.getInstance().insertIrToNowBBlock(new Jmp(context.breakBlock));

        } else if (stmtType.equals("Continue")) {
            CFG.getInstance().insertIrToNowBBlock(new Jmp(context.continueRejudgeB));
        } else if (stmtType.equals("Return")) {
            Var res = null;
            for (Node node : sons) {
                node.buildCFG(context, ret);
                if (node.getType() == TokenType.Exp) {
                    res = ret.res;
                }
            }
            if (res != null) {
                CFG.getInstance().insertIrToNowBBlock(new Return(res));
            } else {
                Var v = new Var("void");
                CFG.getInstance().insertIrToNowBBlock(new Return(v));
            }
        } else if (stmtType.equals("Printf")) {
            ArrayList<Var> pargs = new ArrayList<>();
            String fmtStr = "";
            for (Node node : sons) {
                node.buildCFG(context, ret);
                if (node instanceof Exp) {
                    pargs.add(ret.res);
                }
                if (node instanceof Token && node.getType() == TokenType.FMT_STR) {
                    fmtStr = ((Token) node).getContent();
                }
            }
            String tmpStr = "";
            for (int i = 1, j = 0; i < fmtStr.length() - 1; i++) {
                char nowChar = fmtStr.charAt(i);
                char nextChar = fmtStr.charAt(i + 1);
                if (nowChar == '%' && nextChar == 'd') {

                    if (!tmpStr.isEmpty()) {
                        Var label = Var.makeIrStrId();
                        SymTab.getInstance().addGlobStr(label, tmpStr);
                        tmpStr = "";
                        CFG.getInstance().insertIrToNowBBlock(new PrintStr(label));
                    }
                    CFG.getInstance().insertIrToNowBBlock(new PrintInt(pargs.get(j)));
                    i++; // pass d
                    j++; // pass parg
                } else {
                    tmpStr += nowChar;
                }

            }

            if (!tmpStr.isEmpty()) {
                Var label = Var.makeIrStrId();
                SymTab.getInstance().addGlobStr(label, tmpStr);
                tmpStr = "";
                CFG.getInstance().insertIrToNowBBlock(new PrintStr(label));
            }
        } else if (stmtType.equals("Block")) {
            SymTab.getInstance().beginScope();
            for (Node son : sons) {
                son.buildCFG(context, ret);
            }
            SymTab.getInstance().endScope();
        } else {
            ;
        }

        // 第一句
        if (StmtsInLoop != 0) {
            CFG.getInstance().setNowBBlock(context.continueRejudgeB);
            // 此处再次对cond rejudge
            context.rejudgeCond.buildCFG(context, ret);
            CFG.getInstance().insertIrToNowBBlock(new JmpTrue(now, ret.res));
        } else if (saveJ2Else != 0) {
            CFG.getInstance().insertIrToNowBBlock(new Jmp(context.endBlock));
        }
        context.continueRejudgeB = saveContinueRejudgeBlock;

    }

    private boolean fmtChar(char nowchar, char nextchar) {
        return nowchar == '%' && nextchar == 'd';
    }

    private boolean normalChar(char nowchar, char nextchar) {
        return nowchar == 32 || nowchar == 33 || (40 <= nowchar && nowchar <= 126 && nowchar != 92) || (nowchar == 92 && nextchar == 'n');
    }

    private boolean normalLastChar(char nowchar) {
        return nowchar == 32 || nowchar == 33 || (40 <= nowchar && nowchar <= 126 && nowchar != 92);
    }
}
