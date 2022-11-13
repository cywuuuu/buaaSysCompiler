import IRGen.CFG.CFG;
import Lexer.Cursor;
import Lexer.Lexer;
import Lexer.Source;
import Lexer.LexicalException;
import Lexer.Token;
import Lexer.TokenType;
import Nodes.Node;
import Parser.Error.ParserException;
import Parser.Parser;
import SymTab.Context;
import SymTab.ErrorRecord;
import SymTab.Ret;
import SymTab.SymTab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String file = "testfile.txt";

        //实例化File类

        File fileoutE = new File("error.txt");
        String outpath = "output.txt";
        //实例化File类

        File fileoutO = new File("output.txt");
        File fileoutM = new File("mips.txt");
        File fileoutI = new File("ir.txt");


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outpath));
        Source source = new Source(reader);
        Cursor cursor = new Cursor(source);
        Lexer lexer = new Lexer(cursor);

        ArrayList<Token> tokens = new ArrayList<Token>();
        try {

            while (true) {
                tokens.add(lexer.consumeToken());
            }


        } catch (EOFException e) {
            tokens.add(new Token(TokenType.Error, "END", source.lineNum()));
            reader.close();
            bufferedWriter.close();
        } catch (LexicalException e) {
            e.printStackTrace();
        }

        Parser parser = new Parser(tokens);

        try {
            parser.parseCompUnit();
        } catch (ParserException e) {
            e.printStackTrace();
        }
        Node root = parser.astRoot();
        root.checkError(new Context(), new Ret());
//        setOutputFile(fileoutO);
//        System.out.println(ErrorRecord.getInstance().toString());
//        System.out.println(root.toString());

        root.buildCFG(new Context(), new Ret());
        CFG.getInstance().regAllocate();
        String data = SymTab.getInstance().getGlobalVar2Str();
        String irtext = CFG.getInstance().toString();
        String text = CFG.getInstance().toAsm();

//        setOutputFile(fileoutI);
//        System.out.println(data);
//        System.out.println(irtext);

        setOutputFile(fileoutM);
        System.out.println(data);
        System.out.println(text);
    }

    private static void setOutputFile(File fileoutO) throws FileNotFoundException {
        PrintStream stream = new PrintStream(fileoutO);
        System.setOut(stream);
    }
}
