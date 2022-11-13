package SymTab;

import IRGen.CFG.BBlock;
import Nodes.Node;

public class Context {
    public int inLoop = 0, inVoid = 0, inGlob = 0, inConstDecl = 0, inLVal = 0;
    public String result = "";
    public Node rejudgeCond = null;
    public BBlock breakBlock, continueRejudgeB;

    // continueB are a place to jump out and rejudge


    public BBlock endBlock;
    public int j2Else = 0;

    public Context(int inLoop, int inVoid, int inGlob, int inConstDecl, int inLVal) {
        this.inLoop = inLoop;
        this.inVoid = inVoid;
        this.inGlob = inGlob;
        this.inConstDecl = inConstDecl;
        this.inLVal = inLVal;
    }

    public Context() {
    }
}
