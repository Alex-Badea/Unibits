package parser.grammar;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by balex on 23.04.2018.
 */
public class SequenceOfSymbols extends LinkedList<Symbol> {
    static final SequenceOfSymbols EPSILON_SEQUENCE = new SequenceOfSymbols(new Symbol("EPSILON_ABUSE_OF_NOTATION") {
        @Override
        public boolean isTerminal() {
            throw new UnsupportedOperationException("ε nu este simbol.");
        }
    });

    ////
    public SequenceOfSymbols(Collection<? extends Symbol> c) {
        super(c);
        /*if (c.isEmpty())
            throw new UnsupportedOperationException(this.getClass().getCanonicalName() + "::" + new Object() {
            }.getClass().getEnclosingMethod().getName() + " nu se poate crea un parser.grammar.SequenceOfSymbols gol");
        */
    }

    public SequenceOfSymbols(Symbol... symbols) {
        this(Arrays.asList(symbols));
    }

    boolean isEpsilonSequence() {
        return EPSILON_SEQUENCE.equals(this);
    }

    boolean containsAnyTerminal() {
        return !isEpsilonSequence() && stream().anyMatch(Symbol::isTerminal);
    }

    Set<Nonterminal> getNonterminalsSet() {
        return this.stream().filter(s -> !s.isTerminal()).map(Nonterminal.class::cast).collect(Collectors.toSet());//TODO filter out terminals, make set
    }

    @Nullable
    Set<SequenceOfSymbols> getSequencesToRightOf(Nonterminal nt) {
        if (!this.contains(nt))
            return null;
        var result = new LinkedHashSet<SequenceOfSymbols>();
        for (var seekMatchInd = 0; seekMatchInd < this.size(); seekMatchInd++) {
            if (this.get(seekMatchInd).equals(nt)) {
                result.add(new SequenceOfSymbols(this.subList(seekMatchInd + 1, this.size())));
            }
        }
        return result;
    }

    SequenceOfSymbols subSequence(int fromIndex, int toIndex) {
        return new SequenceOfSymbols(super.subList(fromIndex, toIndex));
    }

    @Override
    public String toString() {
        return this.equals(EPSILON_SEQUENCE) ? Grammar.EPSILON_NOTATION : "\"" + super.toString().substring(1, super.toString().length() - 1).replace(", ", " ") + "\"";
    }
}
