package parser.grammar;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Created by balex on 23.04.2018.
 */
public class Production {
    private final Nonterminal producer;
    private final SequenceOfSymbols symbols;

    ////
    private Production(Nonterminal producer) {
        this.producer = producer;
        symbols = SequenceOfSymbols.EPSILON_SEQUENCE;
    }

    public Production(Nonterminal producer, @NotNull SequenceOfSymbols symbols) {
        assert !symbols.equals(SequenceOfSymbols.EPSILON_SEQUENCE);
        this.producer = producer;
        this.symbols = symbols;
    }

    static Production createEpsilonProduction(Nonterminal producer) {
        return new Production(producer);
    }

    public Nonterminal getProducer() {
        return producer;
    }

    public SequenceOfSymbols getSymbols() {
        return symbols;
    }

    public Symbol getSymbolAt(int index) {
        return symbols.get(index);
    }

    public boolean isEpsilonProduction() {
        return symbols.isEpsilonSequence();
    }

    @Nullable
    Set<SequenceOfSymbols> getSequencesToRightOf(Nonterminal nt) {
        return symbols.getSequencesToRightOf(nt);
    }

    public SequenceOfSymbols getAllAfterIndex(int index) {
        return new SequenceOfSymbols(symbols.subList(index, symbols.size()));
    }


    public int getLength() {
        return symbols.size();
    }

    @Override
    public String toString() {
        return producer + "->" + symbols.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(producer, that.producer) &&
                Objects.equals(symbols, that.symbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producer, symbols);
    }

    @Deprecated
    public String putDotGetString(float index) {
        SequenceOfSymbols left = symbols.subSequence(0, (int) (index + 1));
        SequenceOfSymbols right = symbols.subSequence((int) (index + 1), symbols.size());

        return producer + "->" + left + "•" + right;
    }
}
