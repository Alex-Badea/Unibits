package parser;

import org.jetbrains.annotations.Nullable;

import parser.grammar.*;

import java.util.Objects;
import java.util.Set;

public class Item {
    private final Production production;
    private final float dotPosition;
    private final Terminal lookahead;
    private final boolean acceptor;

    ////
    private Item(Production production, float dotPosition, Terminal lookahead, boolean acceptor) {
        assert Math.abs(dotPosition) - 0.5 == (int) dotPosition;
        assert dotPosition >= -0.5;
        this.production = production;
        this.dotPosition = dotPosition;
        this.lookahead = lookahead;
        this.acceptor = acceptor;
    }

    Item(Production production, Terminal lookahead) {
        this.production = production;
        dotPosition = -0.5f;
        this.lookahead = lookahead;
        acceptor = false;
    }

    Production getProduction() {
        return production;
    }

    Terminal getLookahead() {
        return lookahead;
    }

    @Nullable
    Symbol getLook() {
        assert (int) (1 + dotPosition) <= production.getLength();
        if ((int) (1 + dotPosition) == production.getLength())
            return null;
        return production.getSymbolAt((int) (1 + dotPosition));
    }

    SequenceOfSymbols getAllAfterLook() {
        return production.getAllAfterIndex((int) (2 + dotPosition));
    }

    static State assembleMultiple(Production production, Set<Terminal> lookaheads) {
        assert !lookaheads.isEmpty();
        var result = new State();
        for (var la : lookaheads) {
            result.add(new Item(production, la));
        }
        return result;
    }

    boolean isEpsilonItem() {
        return production.isEpsilonProduction();
    }

    @Nullable
    Item consume(Symbol sym) {
        if (this.getLook() == null)
            return null;
        if (this.getLook().equals(sym))
            if (Terminal.EOF_TERMINAL.equals(sym))
                return new Item(production, dotPosition + 1, lookahead, true);
            else
                return new Item(production, dotPosition + 1, lookahead, false);
        else
            return null;
    }

    boolean isFinal() {
        return getLook() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Float.compare(item.dotPosition, dotPosition) == 0 &&
                Objects.equals(production, item.production) &&
                Objects.equals(lookahead, item.lookahead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(production, dotPosition, lookahead);
    }

    @Override
    public String toString() {
        return "[" + production.putDotGetString(dotPosition) + ", " + lookahead + "]";
    }
}
