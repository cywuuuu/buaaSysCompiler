package SymTab;

import IRGen.IRs.Var;
import Lexer.TokenType;

import java.util.ArrayList;

public class Ret {
    public int vardim = 0, val = 0, hasConst = 0, hasRet = 0, funcTypeVoid = 0;
    // vardim Lval - Exp
    // val Exp
    // hasConst 查表Lval - Stmtcheck


    public Var res;
    public Var param;
    public int isArray = 0;
    public TokenType op = TokenType.PLUS;
    public ArrayList<Integer> dims = new ArrayList<>();
    public ArrayList<Var> args = new ArrayList<>();
    public ArrayList<Integer> initVal = new ArrayList<>();
    public ArrayList<Var> initStr = new ArrayList<>();


    public Ret() {
    }

    public void clearOp() { op = TokenType.PLUS; }

    public void clearArgs() {
        args.clear();
    }

    public void clearInitVal() {
        initVal.clear();
    }

    public void clearIsArray() { isArray = 0; }

    public void clearRes() {
        res = null;
    }

    public void clearParam() {
        param = null;
    }

    public Ret(int vardim, int val, int hasConst, int hasRet, int funcTypeVoid) {
        this.vardim = vardim;
        this.val = val;
        this.hasConst = hasConst;
        this.hasRet = hasRet;
        this.funcTypeVoid = funcTypeVoid;
    }

    public void clearVardim() {
        this.vardim = 0;
    }

    public void clearVal() {
        this.val = 0;
    }

    public void clearHasConst() {
        this.hasConst = 0;
    }

    public void clearHasRet() {
        this.hasRet = 0;
    }

    public void clearFuncTypeVoid() {
        this.funcTypeVoid = 0;
    }

    public void setDims(ArrayList<Integer> dims) {
        this.dims = dims;
    }

    public void clearDims() {
        this.dims.clear();
    }

}
