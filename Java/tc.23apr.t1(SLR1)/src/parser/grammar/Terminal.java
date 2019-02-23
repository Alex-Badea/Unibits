package parser.grammar;

/**
 * Created by balex on 23.04.2018.
 */
public class Terminal extends Symbol {
    ////
    public static final Terminal EOF_TERMINAL = new Terminal(Grammar.EOF_NOTATION, null);

    @SuppressWarnings("unused")
    private Terminal(String name, Void dummy) {
        super(name);
    }

    public Terminal(String name) {
        super(name);
        if (Grammar.EOF_NOTATION.equals(name))
            throw new RuntimeException("Nu se poate crea nonterminalul EOF; Folosește Terminal.EOF_TERMINAL");
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
