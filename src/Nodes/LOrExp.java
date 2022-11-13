package Nodes;

import IRGen.CFG.BBlock;
import IRGen.CFG.CFG;
import IRGen.IRs.JmpFalse;
import IRGen.IRs.JmpTrue;
import IRGen.IRs.Move;
import IRGen.IRs.Var;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

public class LOrExp extends Node {
    public LOrExp(int lineNum1, int lineNum2, TokenType type) {
        super(lineNum1, lineNum2, type);
    }

    @Override
    public void checkError(Context context, Ret ret) {
        for (Node son : sons) {
            son.checkError(context, ret);

        }
    }

    //    @Override
//    public void buildCFG(Context context, Ret ret) {
//        TokenType t = null;
//        String res = "";
//        String l = "";
//        String r = "";
//        boolean lCalculable = false;
//        int a = 0;
//        int b = 0;
//        BBlock ifCondEnterBlock = null;
//        if (sons.size() > 1) {
//
//            // a || b
//            // 1. 若a,b 都可以算出来， 直接出结果， IR则直接把branch化简，四代码删除
//            // 2. a或b不可算，a不可算时，插入ifcondEntrance 引导alu计算并判断是否短路直接跳转， b不可算则，直接坠入ifcond
//
//            sons.get(0).buildCFG(context, ret);
//            l = ret.res;
//            sons.get(2).buildCFG(context, ret);
//            r = ret.res;
//            boolean lcal = false;
//            boolean rcal = false;
//            try {
//                a = Integer.parseInt(l);
//                lcal = true;
//            } catch (NumberFormatException e) {
//                lcal = false;
//            }
//
//            try {
//                b = Integer.parseInt(r);
//                rcal = true;
//            } catch (NumberFormatException e) {
//                rcal = false;
//            }
//
//            if (lcal && rcal) {
//                if (a == 1 || b == 1) {
//                    ret.res = "1";
//                } else {
//                    ret.res = "0";
//                }
//                return;
//            }
//            if (lcal && !rcal) {
//                if (a == 1) {
//                    ret.res = "1";
//                    return;
//                }
//                // 如果 a == 0 没用，直接去掉看 varb
//                res = Var.makeIrTmpId();
//                CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(r)));//存进temp看ifcond，自动滑落，是否妈祖
//            }
//            if (!lcal && rcal) {
//                if (b == 1) {
//                    ret.res = "1";
//                    return;
//                }
//                ifCondEnterBlock = new BBlock(); // 目标ifblockEntrence 预留出来,短路j到入口直接求值
//                res = Var.makeIrTmpId();
//                CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(l)));
//                CFG.getInstance().insertIrToNowBBlock(new JmpTrue(ifCondEnterBlock, new Var(res))); //行的话保存结果到res,然后直接去ifcond判断
//
//                BBlock calRBlock = new BBlock();
//                CFG.getInstance().setNowBBlock(calRBlock); //后续算R在 calRBlock中计算，其实不用算了，可以去掉后面了
//
//
//                CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(r)));
//                CFG.getInstance().setNowBBlock(ifCondEnterBlock);
//
//
//            }
//            if (!lcal && !rcal) {
//                ifCondEnterBlock = new BBlock(); // 目标ifblock 预留出来
//                res = Var.makeIrTmpId();
//                CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(l)));
//                CFG.getInstance().insertIrToNowBBlock(new JmpTrue(ifCondEnterBlock, new Var(res))); //行的话保存结果到res,然后直接去ifcond判断
//
//                BBlock calRBlock = new BBlock();
//                CFG.getInstance().setNowBBlock(calRBlock); //后续算R在 calRBlock中计算
//
//                CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(r)));
//                CFG.getInstance().setNowBBlock(ifCondEnterBlock);
//            }
//
//
//        } else {
//            sons.get(0).buildCFG(context, ret);
//            l = ret.res;
//
//            boolean lcal = false;
//
//            try {
//                a = Integer.parseInt(l);
//                lcal = true;
//            } catch (NumberFormatException e) {
//            }
//
//
//            if (lcal) {
//                if (a == 1) {
//                    ret.res = "1";
//                } else {
//                    ret.res = "0";
//                }
//                return;
//            }
//            // !lcal
//            // 如果 a == 1 没用，直接去掉看 varb
//            res = Var.makeIrTmpId();
//            CFG.getInstance().insertIrToNowBBlock(new Move(new Var(res), new Var(r)));//存进temp看ifcond，自动滑落，是否妈祖
//
//
//        }
//
//
//        ret.res = res;
//
//    }
//    @Override
//    public void buildCFG(Context context, Ret ret) {
//        TokenType t = null;
//        Var res = null;
//        Var l = null;
//        Var r = null;
//        if (sons.size() >= 3) {
//            boolean lCalculable = false;
//            sons.get(0).buildCFG(context, ret);
//            l = ret.res;
//            int a = 0;
//            int b = 0;
//            BBlock ifCondEnterBlock = null;
//            try {
//                a = Integer.parseInt(l.getName());
//                if (a != 0) {
//                    // 短路
//                    ret.res = new Var("1");
//                    return; // 无事发生继续执行指令即可
//                }
//                // 可算但不短路， 不用算这个了，直接看右侧
//                lCalculable = true;
//            } catch (NumberFormatException e) {
//                // 计算不出来左部
//                ifCondEnterBlock = new BBlock("ifEnterJudgeBlock"); // 目标ifblock 预留出来
//                res = Var.makeIrTmpId();
//                CFG.getInstance().insertIrToNowBBlock(new Move(res, l));
//                CFG.getInstance().insertIrToNowBBlock(new JmpTrue(ifCondEnterBlock, res)); //行的话保存结果到res,然后直接去ifcond判断
//
//                BBlock calRBlock = new BBlock("LOrCondCalR");
//                CFG.getInstance().setNowBBlock(calRBlock); //后续算R在 calRBlock中计算
//                lCalculable = false;
//            }
//            if (sons.size() > 1) {
//                sons.get(2).buildCFG(context, ret);
//                r = ret.res;
//
//
//                if (lCalculable) {
//                    // 若左部可算时
//                    try {
//                        // 右部可算
//                        b = Integer.parseInt(r.getName());
//                        if (b != 0) {
//                            ret.res = new Var("1");
//                            return; // 无事发生继续执行指令即可, 自动会进入ifcond
//
//                        } else {
//                            ret.res = new Var("0");
//                            return;
//                        }
//
//                    } catch (NumberFormatException e) {
//                        // 计算不出来右部, 存到里面让自动落下ifcond去判断
//
//                        res = Var.makeIrTmpId();
//
//                    }
//                } else {
//                    try {
//                        b = Integer.parseInt(r.getName());
////                        if (b != 0) {
////                            // 短路
////                            ret.res = new Var("1");
////                            return; // 无事发生继续执行指令即可, 自动落下 ifcond去判断结果是否进入
////                        }
//
//                    } catch (NumberFormatException e) {
//                        ;
//                    }
//                }
//                CFG.getInstance().insertIrToNowBBlock(new Move(res, r));
//                if (!lCalculable) {
//                    // 若左侧不可算，结果存在了var里,需要前面的j到了ifcondentrance去提前判下左测
//                    // 右侧自动落下放在在ifcondentrance后面中判断结果
//                    CFG.getInstance().setNowBBlock(ifCondEnterBlock);
//                }
//
//
//                // a || b
//                // 1. 若a,b 都可以算出来， 直接出结果， IR则直接把branch化简，四代码删除
//                // 2. a或b不可算，a不可算时，插入ifcondEntrance 引导alu计算并判断是否短路直接跳转， b不可算则，直接坠入ifcond
//
//
//            }
//
//
//            ret.res = res;
//        } else if (!sons.isEmpty()) {
//            sons.get(0).buildCFG(context, ret);
//        }
//
//
//    }
    @Override
    public void buildCFG(Context context, Ret ret) {
        TokenType t = null;
        Var res = null;
        Var l = null;
        Var r = null;
        if (sons.size() >= 3) {
            boolean lCalculable = false;
            sons.get(0).buildCFG(context, ret);
            l = ret.res;
            int a = 0;
            int b = 0;
            BBlock ifCondEnterBlock = null;
            try {
                a = Integer.parseInt(l.getName());
                if (a != 0) {
                    // 短路
                    ret.res = new Var("1");
                    return; // 无事发生继续执行指令即可
                }
                // 可算但不短路， 不用算这个了，直接看右侧
                lCalculable = true;
            } catch (NumberFormatException e) {
                // 计算不出来左部
                ifCondEnterBlock = new BBlock("ifJudgeBlock"); // 目标ifblock 预留出来
                res = Var.makeIrTmpId();
                CFG.getInstance().insertIrToNowBBlock(new Move(res, l));
                CFG.getInstance().insertIrToNowBBlock(new JmpTrue(ifCondEnterBlock, res)); //行的话保存结果到res,然后直接去ifcond判断

                BBlock calRBlock = new BBlock("LOrCondCalR");
                CFG.getInstance().setNowBBlock(calRBlock); //后续算R在 calRBlock中计算
                lCalculable = false;
            }
            // 左部算完，未发生短路，结果全看右部
            sons.get(2).buildCFG(context, ret);
            r = ret.res;

            if (lCalculable) {
                try {
                    // 右部可算
                    b = Integer.parseInt(r.getName());
                    if (b != 0) {
                        ret.res = new Var("1");
                        return; // 无事发生继续执行指令即可, 自动会进入ifcond
                    } else {
                        ret.res = new Var("0");
                        return;
                    }

                } catch (NumberFormatException e) {
                    // 左部可算，计算不出来右部, 未发生短路时，右部即结果，存到res里面
                    // 让自动落下ifcond去判断
                    res = Var.makeIrTmpId();
                    CFG.getInstance().insertIrToNowBBlock(new Move(res, r));
                }

            } else {
                // 若左侧不可算，右部不可算，未发生短路，结果存在了var里,需要前面的j到了ifcondentrance去提前判下左测
                // 右侧自动落下放在在ifcondentrance后面中判断结果
                CFG.getInstance().insertIrToNowBBlock(new Move(res, r));
                CFG.getInstance().setNowBBlock(ifCondEnterBlock);
            }


            ret.res = res;
        } else if (!sons.isEmpty()) {
            sons.get(0).buildCFG(context, ret);
        }

    }
}
