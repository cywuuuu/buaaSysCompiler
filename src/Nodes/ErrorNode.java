package Nodes;

import Lexer.TokenType;
import Parser.Error.ParserException;
import SymTab.Context;
import SymTab.ErrorRecord;
import SymTab.Ret;

public class ErrorNode extends Node{
    ParserException e;

    public ErrorNode(int lineNum1, int lineNum2, ParserException e) {
        super(lineNum1, lineNum2, TokenType.Error);
        this.e = e;
        e.setLine(lineNum1);
    }

    @Override
    public void checkError(Context context, Ret ret) {
        ErrorRecord.getInstance().recordError(e);
    }
}
