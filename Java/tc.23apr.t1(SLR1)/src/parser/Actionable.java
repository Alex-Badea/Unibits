package parser;

import parser.grammar.Production;
import parser.grammar.Symbol;
import parser.grammar.Terminal;

import java.util.Set;

public interface Actionable {
    State getCurrentState();

    default void accept() {
        System.out.println("Secvență acceptată!");
    }

    default void error(State state, Terminal received, Set<Symbol> expected) {
        throw new RuntimeException("Eroare de parsare!\nStarea " + state + ": primit " + received + ", așteptat " + expected);
    }

    void reduce(Production reduction);

    void goTo(State where);

    void shift(State where, Terminal with);
}
