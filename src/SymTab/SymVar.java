package SymTab;

import IRGen.IRs.Var;
import Nodes.InitVal;

import java.util.ArrayList;

public class SymVar extends SymElement {
    private boolean globVar;
    private boolean constVar;
    public String ident;
    public Var varId;
    private ArrayList<Integer> varSizes = new ArrayList<>();
    private ArrayList<Integer> init = new ArrayList<>();

    public SymVar(boolean globVar, boolean constVar, String ident, ArrayList<Integer> dims, ArrayList<Integer> init, Var varId) {
        this.globVar = globVar;
        this.constVar = constVar;
        this.ident = ident;
        this.varSizes = dims;
        this.init = init;
        this.varId = varId;
    }

    public boolean isGlobVar() {
        return globVar;
    }

    public boolean isConstVar() {
        return constVar;
    }

    public String getIdent() {
        return ident;
    }

    public ArrayList<Integer> getVarSizes() {
        return varSizes;
    }

    public int getVarDim() {
        return varSizes.size();
    }


    public ArrayList<Integer> getInit() {
        return init;
    }

    public Integer getArrVal(ArrayList<Integer> pos) {
        int offset = calOffset(pos);
        if (init.size() == 0) return 0;
        if (init.size() < offset) System.out.println("Runtime Err OutOfBound");
        return init.size() > offset ? init.get(offset) : 0;
    }

    public Integer calOffset(ArrayList<Integer> pos) {
        if (pos.size() == 0) {
            return 0;
        } else {
            int base = 1;
            int offset = 0;
            for (int i = this.varSizes.size() - 1; i >= pos.size(); i--) {
                base = base * varSizes.get(i);
            }

            for (int i = pos.size() - 1; i >= 0; i--) {
                offset += base * pos.get(i);
                base = base * varSizes.get(i);
            }
            return offset;
        }
    }

    public String getVar2Str() {
        StringBuilder sb = new StringBuilder();
        int size = varSizes.stream().reduce(1, (acc, n) -> acc * n);

        if (init.isEmpty()) {
            // a[9] init is empity but repeat for sizes time
            sb.append(String.format("0: %d\n", size)); // 0 initVal : 1 repeat time
        } else {
            for (int val: init) {
                sb.append(String.format(" %d ", val));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
