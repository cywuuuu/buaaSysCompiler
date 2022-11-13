package Parser;


import Lexer.Token;
import Lexer.TokenType;
import Nodes.AddExp;
import Nodes.Block;
import Nodes.CompUnit;
import Nodes.Cond;
import Nodes.ConstDecl;
import Nodes.ConstDef;
import Nodes.ConstExp;
import Nodes.ConstInitVal;
import Nodes.EqExp;
import Nodes.ErrorNode;
import Nodes.Exp;
import Nodes.FuncDef;
import Nodes.FuncFParam;
import Nodes.FuncFParams;
import Nodes.FuncRParams;
import Nodes.FuncType;
import Nodes.InitVal;
import Nodes.LAndExp;
import Nodes.LOrExp;
import Nodes.LVal;
import Nodes.MainFuncDef;
import Nodes.MulExp;
import Nodes.Node;
import Nodes.Number;
import Nodes.PrimaryExp;
import Nodes.RelExp;
import Nodes.Stmt;
import Nodes.UnaryExp;
import Nodes.UnaryOp;
import Nodes.VarDecl;
import Nodes.VarDef;
import Parser.Error.ParserException;
import Parser.Error.UndefinedError;

import java.util.Stack;

public class AstBuilder {
    public class Pair<K, V> {
        private K key;

        public K getKey() {
            return key;
        }

        private V value;

        public V getValue() {
            return value;
        }

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    Stack<Pair<TokenType, Integer>> posRecord = new Stack<>();
    Stack<Node> nodeStack = new Stack<>();  //contains all processed& not inserted node
    private int lineCnt;

    public AstBuilder() {
    }

    public Node startBld(TokenType type, int beginline) {
        Node node = null;
        if (type == TokenType.AddExp) {
            node = new AddExp(beginline, beginline, type);
        } else if (type == TokenType.Block) {
            node = new Block(beginline, beginline, type);
        } else if (type == TokenType.CompUnit) {
            node = new CompUnit(beginline, beginline, type);
        } else if (type == TokenType.Cond) {
            node = new Cond(beginline, beginline, type);
        } else if (type == TokenType.ConstDecl) {
            node = new ConstDecl(beginline, beginline, type);
        } else if (type == TokenType.ConstDef) {
            node = new ConstDef(beginline, beginline, type);
        } else if (type == TokenType.ConstExp) {
            node = new ConstExp(beginline, beginline, type);
        } else if (type == TokenType.ConstInitVal) {
            node = new ConstInitVal(beginline, beginline, type);
        } else if (type == TokenType.EqExp) {
            node = new EqExp(beginline, beginline, type);
        } else if (type == TokenType.Exp) {
            node = new Exp(beginline, beginline, type);
        } else if (type == TokenType.FuncDef) {
            node = new FuncDef(beginline, beginline, type);
        } else if (type == TokenType.FuncFParam) {
            node = new FuncFParam(beginline, beginline, type);
        } else if (type == TokenType.FuncFParams) {
            node = new FuncFParams(beginline, beginline, type);
        } else if (type == TokenType.FuncRParams) {
            node = new FuncRParams(beginline, beginline, type);
        } else if (type == TokenType.FuncType) {
            node = new FuncType(beginline, beginline, type);
        } else if (type == TokenType.InitVal) {
            node = new InitVal(beginline, beginline, type);
        } else if (type == TokenType.LAndExp) {
            node = new LAndExp(beginline, beginline, type);
        } else if (type == TokenType.LOrExp) {
            node = new LOrExp(beginline, beginline, type);
        } else if (type == TokenType.LVal) {
            node = new LVal(beginline, beginline, type);
        } else if (type == TokenType.MainFuncDef) {
            node = new MainFuncDef(beginline, beginline, type);
        } else if (type == TokenType.MulExp) {
            node = new MulExp(beginline, beginline, type);
        } else if (type == TokenType.Number) {
            node = new Number(beginline, beginline, type);
        } else if (type == TokenType.PrimaryExp) {
            node = new PrimaryExp(beginline, beginline, type);
        } else if (type == TokenType.RelExp) {
            node = new RelExp(beginline, beginline, type);
        } else if (type == TokenType.Stmt) {
            node = new Stmt(beginline, beginline, type);
        } else if (type == TokenType.UnaryExp) {
            node = new UnaryExp(beginline, beginline, type);
        } else if (type == TokenType.UnaryOp) {
            node = new UnaryOp(beginline, beginline, type);
        } else if (type == TokenType.VarDecl) {
            node = new VarDecl(beginline, beginline, type);
        } else if (type == TokenType.VarDef) {
            node = new VarDef(beginline, beginline, type);
        }


        posRecord.push(new Pair<>(type, nodeStack.size()));
        return node;
    }

    public void insertTerm(Token token) {

        nodeStack.push(token);
    }

    public Node insertNode(TokenType type, int beginline, int pos) {
        Node node = null;
        if (type == TokenType.AddExp) {
            node = new AddExp(beginline, beginline, type);
        } else if (type == TokenType.Block) {
            node = new Block(beginline, beginline, type);
        } else if (type == TokenType.CompUnit) {
            node = new CompUnit(beginline, beginline, type);
        } else if (type == TokenType.Cond) {
            node = new Cond(beginline, beginline, type);
        } else if (type == TokenType.ConstDecl) {
            node = new ConstDecl(beginline, beginline, type);
        } else if (type == TokenType.ConstDef) {
            node = new ConstDef(beginline, beginline, type);
        } else if (type == TokenType.ConstExp) {
            node = new ConstExp(beginline, beginline, type);
        } else if (type == TokenType.ConstInitVal) {
            node = new ConstInitVal(beginline, beginline, type);
        } else if (type == TokenType.EqExp) {
            node = new EqExp(beginline, beginline, type);
        } else if (type == TokenType.Exp) {
            node = new Exp(beginline, beginline, type);
        } else if (type == TokenType.FuncDef) {
            node = new FuncDef(beginline, beginline, type);
        } else if (type == TokenType.FuncFParam) {
            node = new FuncFParam(beginline, beginline, type);
        } else if (type == TokenType.FuncFParams) {
            node = new FuncFParams(beginline, beginline, type);
        } else if (type == TokenType.FuncRParams) {
            node = new FuncRParams(beginline, beginline, type);
        } else if (type == TokenType.FuncType) {
            node = new FuncType(beginline, beginline, type);
        } else if (type == TokenType.InitVal) {
            node = new InitVal(beginline, beginline, type);
        } else if (type == TokenType.LAndExp) {
            node = new LAndExp(beginline, beginline, type);
        } else if (type == TokenType.LOrExp) {
            node = new LOrExp(beginline, beginline, type);
        } else if (type == TokenType.LVal) {
            node = new LVal(beginline, beginline, type);
        } else if (type == TokenType.MainFuncDef) {
            node = new MainFuncDef(beginline, beginline, type);
        } else if (type == TokenType.MulExp) {
            node = new MulExp(beginline, beginline, type);
        } else if (type == TokenType.Number) {
            node = new Number(beginline, beginline, type);
        } else if (type == TokenType.PrimaryExp) {
            node = new PrimaryExp(beginline, beginline, type);
        } else if (type == TokenType.RelExp) {
            node = new RelExp(beginline, beginline, type);
        } else if (type == TokenType.Stmt) {
            node = new Stmt(beginline, beginline, type);
        } else if (type == TokenType.UnaryExp) {
            node = new UnaryExp(beginline, beginline, type);
        } else if (type == TokenType.UnaryOp) {
            node = new UnaryOp(beginline, beginline, type);
        } else if (type == TokenType.VarDecl) {
            node = new VarDecl(beginline, beginline, type);
        } else if (type == TokenType.VarDef) {
            node = new VarDef(beginline, beginline, type);
        }
        posRecord.push(new Pair<TokenType, Integer>(type, pos));
        return node;
    }

    public void endBld(Node node, int endline) {
        Pair<TokenType, Integer> p = posRecord.pop();
        node.setType(p.key);
        node.setLineNum2(endline);
        while (nodeStack.size() > p.value) {
            Node node1 = nodeStack.pop();
            node.addSon(node1);
        }
//        Pattern pattern = Pattern.compile("class Nodes.(.*)");
//        Matcher matcher = pattern.matcher(node.getClass().toString())
//        System.out.printf("<%s>%n", node.getClass().getSimpleName());
        nodeStack.push(node);
    }

    public void error(ParserException e, int line) {
        Node err = new ErrorNode(line, line, e);
        if (!(e instanceof UndefinedError)) {
            nodeStack.push(err);
            return;
        } else {
            Pair<TokenType, Integer> p = posRecord.pop();
            while (nodeStack.size() > p.value) {
                err.addSon(nodeStack.pop());
            }
            nodeStack.push(err);
        }
    }

    public int posRecord() {
        return nodeStack.size();
    }

    Node root() {
        return nodeStack.get(0);
    }


}
