package parser.grammar;

import java.util.Objects;

/**
 * Created by balex on 23.04.2018.
 */
public abstract class Symbol {
    private final String name;

    ////
    Symbol(String name) {
        this.name = name;
    }

    public abstract boolean isTerminal();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(name, symbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
