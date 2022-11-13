package SymTab;

import IRGen.IRs.Var;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymTab {

    private static SymTab symTab = null;


    private int varCnt = 0;

    public static synchronized SymTab getInstance() {
        if (symTab == null) {
            symTab = new SymTab();
        }
        return symTab;
    }

    Stack<Scope> scopeStack = new Stack<Scope>();
    private HashMap<Var, String> globStr = new HashMap<>();
    private HashMap<Var, SymVar> globVar = new HashMap<>();
    private HashMap<Var, SymVar> constVar = new HashMap<>();

    public void clearAll() {
        scopeStack.clear();
        globVar.clear();
        globStr.clear();
        constVar.clear();
    }

    public String getGlobalVar2Str() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");

        HashMap<Var, SymVar> constGlobVar = new HashMap<>();


        for (Map.Entry<Var, String> e: globStr.entrySet()) {
            sb.append(String.format("%s: .asciiz \"%s\"\n", e.getKey().getName(), e.getValue()));
        }
        constGlobVar.putAll(globVar);
        constGlobVar.putAll(constVar);
        // 去重
        for (Map.Entry<Var, SymVar> e: constGlobVar.entrySet()) {
            sb.append(String.format("%s: .word %s\n", e.getKey().getName(), e.getValue().getVar2Str()));
        }

        return sb.toString();
    }

    public boolean defFunc(String name, ArrayList<Integer> dims, boolean voidFunc) {

        if (scopeStack.peek().hasFunc(name)) {
            return false;
        }

        Scope nowScope = scopeStack.peek();
        try {
            nowScope.addFunc(new SymFunc(deepCopy(dims), voidFunc, name));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean defFuncPrevScope(String name, ArrayList<Integer> dims, boolean voidFunc) {

        if (scopeStack.get(scopeStack.size() - 2).hasFunc(name)) {
            return false;
        }

        Scope nowScope = scopeStack.get(scopeStack.size() - 2);
        try {
            nowScope.addFunc(new SymFunc(deepCopy(dims), voidFunc, name));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean defVar(String name, ArrayList<Integer> arraySizes, ArrayList<Integer> init, boolean constVar) {
        if (scopeStack.peek().hasVar(name)) {
            return false;
        }
        Scope nowScope = scopeStack.peek();
        Var var = Var.makeIrVarId(name);
        try {
            SymVar a = new SymVar(scopeStack.size() == 1, constVar, name, deepCopy(arraySizes), deepCopy(init), var);
            nowScope.addVar(a);
            if (scopeStack.size() == 1) {
                globVar.put(var, a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Var defVarWithIRId(String name, ArrayList<Integer> arraySizes, ArrayList<Integer> init, boolean constVar) {
        if (scopeStack.peek().hasVar(name)) {
            return null;
        }
        Scope nowScope = scopeStack.peek();
        Var var = Var.makeIrVarId(name);
        try {
            SymVar a = new SymVar(scopeStack.size() == 1, constVar, name, deepCopy(arraySizes), deepCopy(init), var);
            nowScope.addVar(a);
            if (scopeStack.size() == 1) {
                globVar.put(var, a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return var;
    }


    public SymFunc getFunc(String name) {
        SymFunc func = null;
        for (int i = scopeStack.size() - 1; i >= 0; i -= 1) {
            Scope scope = scopeStack.get(i);
            if (scope.hasFunc(name)) {
                func = scope.getFunc(name);
                break;
            }
        }


        return func;
    }

    public SymVar getVar(String name) {
        SymVar var = null;
        for (int i = scopeStack.size() - 1; i >= 0; i -= 1) {
            Scope scope = scopeStack.get(i);
            if (scope.hasVar(name)) {
                var = scope.getVar(name);
                break;
            }
        }

        return var;
    }

    public void beginScope() {
        scopeStack.push(new Scope());
    }

    public void endScope() {
        Scope nowScope = scopeStack.peek();
        nowScope.collectConstVars(constVar);
        scopeStack.pop();
    }

    public boolean isGlob() {
        return scopeStack.size() == 1;
    }

    public void addGlobStr(Var label, String content) {
        globStr.put(label, content);
    }

    private ArrayList<Integer> deepCopy(ArrayList<Integer> srcList) throws IOException, ClassNotFoundException {
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(srcList);

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayList<Integer> desList = (ArrayList<Integer>) ois.readObject();
        return desList;
    }
}
