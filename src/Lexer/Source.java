package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Source {
    ArrayList<Integer> lineCnt = new ArrayList<>();
    String code = "";
    ArrayList<String> lines = new ArrayList<>();

    public Source(BufferedReader reader) {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line + '\n');
                lineCnt.add(line.length() + 1);
                code += (line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int char2lineNum(int charNum) {
        int i = 1;
        for (int length : lineCnt) {
            charNum -= length;
            if (charNum < 0) break;
            i++;
        }
        return i;
    }

    public int lineNum() {
        return lines.size() ;
    }

    int begin() {
        return 0;
    }

    int end() {
        return code.length();
    }

    char charAt(int n) throws StringIndexOutOfBoundsException {
        return code.charAt(n);
    }

    String pos2Str(int l, int r) throws StringIndexOutOfBoundsException {
        return code.substring(l, r);
    }

}
