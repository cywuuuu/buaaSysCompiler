package SymTab;

import IRGen.IRs.Var;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Scope {
    HashMap<String, SymFunc> funcs = new HashMap<String, SymFunc>();
    HashMap<String, SymVar> vars = new HashMap<String, SymVar>();


    boolean hasFunc(String name) {
        return funcs.containsKey(name) || vars.containsKey(name);
    }

    boolean hasVar(String name) {
        return vars.containsKey(name) || funcs.containsKey(name);
    }

    void addFunc(SymFunc a) {
        funcs.put(a.ident, a);
    }

    void addVar(SymVar a) {
        vars.put(a.ident, a);
    }



    public void collectConstVars(HashMap<Var, SymVar> constMap) {

        for (Map.Entry<String, SymVar> e: vars.entrySet()) {
            if (e.getValue().isConstVar()) {
                constMap.put(e.getValue().varId, e.getValue());
            }
        }
    }

    SymFunc getFunc(String name) {
        return funcs.get(name);
    }

    SymVar getVar(String name) {
        return vars.get(name);
    }
}
