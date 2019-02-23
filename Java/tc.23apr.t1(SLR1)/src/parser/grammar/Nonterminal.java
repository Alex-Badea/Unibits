package parser.grammar;

/**
 * Created by balex on 23.04.2018.
 */
public class Nonterminal extends Symbol {
    ////
    public Nonterminal(String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
