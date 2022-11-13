package Nodes;

import IRGen.IRs.Var;
import Lexer.Token;
import Lexer.TokenType;
import SymTab.Context;
import SymTab.Ret;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
    protected int lineNum1;
    protected int lineNum2;
    protected TokenType type;
    protected ArrayList<Node> sons = new ArrayList<>();

    public Node(int lineNum1, int lineNum2, TokenType type) {
        this.lineNum1 = lineNum1;
        this.lineNum2 = lineNum2;
        this.type = type;
    }

    public Node() {
    }

    public int getLineNum1() {
        return lineNum1;
    }

    public int getLineNum2() {
        return lineNum2;
    }

    public TokenType getType() {
        return type;
    }

    public void addSon(Node a) {
        sons.add(0, a);

    }

    public void setLineNum1(int lineNum1) {
        this.lineNum1 = lineNum1;
    }

    public void setLineNum2(int lineNum2) {
        this.lineNum2 = lineNum2;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void checkError(Context context, Ret ret) {
        for (Node son:sons) {
            son.checkError(context, ret);
        }

    }

    public void buildCFG(Context context, Ret ret) {
        for (Node son:sons) {
            son.buildCFG(context, ret);
        }
    }

    @Override
    public String toString() {
        if (this instanceof Token) {
            return ((Token) this).toString();
        } else {
            StringBuilder b = new StringBuilder();
            for (Node son : sons) {
                b.append(son.toString()).append("\n");
            }
            b.append(String.format("<%s>", this.getClass().getSimpleName()));
            return b.toString();
        }
    }

    public Node deepCopy()  {
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node desList = null;
        try {
            desList = (Node) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return desList;
    }

}


