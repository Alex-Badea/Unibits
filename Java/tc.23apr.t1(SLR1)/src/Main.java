import parser.Parser;
import parser.grammar.Grammar;

import java.io.File;

/**
 * Created by balex on 23.04.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Grammar g = new Grammar(new File("in.txt"));
        Parser p = new Parser(g);
        p.parse("if true then if false then stmt else if true then stmt else stmt else stmt");
        System.err.println("Dacă stiva de simboluri afișează [" + g.getStartNonterminal() + ", $], parsarea a avut succes.");
    }
}

