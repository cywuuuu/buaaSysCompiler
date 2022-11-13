package SymTab;

import Parser.Error.ParserException;

import java.util.ArrayList;
import java.util.Comparator;

public class ErrorRecord {
    private static ErrorRecord errorRecord = null;
    private ArrayList<ParserException> errors = new ArrayList<>();


    public ErrorRecord() {
    }

    public static synchronized ErrorRecord getInstance() {
        if (errorRecord == null) {
            errorRecord = new ErrorRecord();
        }
        return errorRecord;
    }

    public void recordError(ParserException e) {
        errors.add(e);
    }

    public void sortRecord() {
        errors.sort(new Comparator<ParserException>() {
            @Override
            public int compare(ParserException o1, ParserException o2) {
                return Integer.compare(o1.getLine(), o2.getLine());
            }
        });
    }

    public String toString() {
        sortRecord();
        StringBuilder sb = new StringBuilder();
        for (ParserException error : errors) {
            sb.append(error.getLine());
            sb.append(" ");
            sb.append(error.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
