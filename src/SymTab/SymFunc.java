package SymTab;

import Parser.Error.ParamTypeUnmatched;
import Parser.Error.ParamsNumUnmatched;
import Parser.Error.ParserException;

import java.util.ArrayList;

public class SymFunc extends SymElement{
    ArrayList<Integer> dims = new ArrayList<>();
    Boolean voidFunc;
    String ident;

    public SymFunc(ArrayList<Integer> dims, Boolean voidFunc, String ident) {
        this.dims = dims;
        this.voidFunc = voidFunc;
        this.ident = ident;
    }

    public ArrayList<Integer> getDims() {
        return dims;
    }

    public Boolean getVoidFunc() {
        return voidFunc;
    }

    public String getIdent() {
        return ident;
    }

    public ParserException checkParams(ArrayList<Integer> callDims) {
        if (callDims.size() != dims.size()) {
            return new ParamsNumUnmatched();
        } else if (!callDims.equals(dims)) {
            return new ParamTypeUnmatched();
        }
        return null;
    }

}
