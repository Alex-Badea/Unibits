package parser;

import com.sun.istack.internal.Nullable;
import parser.grammar.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class State extends LinkedHashSet<Item>  {

    State() {
        super();
    }

    State(Collection<? extends Item> c) {
        super(c);
    }

    State consume(Symbol sym) {
        State result = new State();
        for (Item it : this)
            if (it.consume(sym) != null)
                result.add(it.consume(sym));
        return result;
    }

    Set<Symbol> getConsumables() {
        return this.stream().map(Item::getLook).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Nullable
    Production getReductionUponLookahead(Terminal lookahead) {
        var result = this.stream().filter(item -> item.isFinal() && item.getLookahead().equals(lookahead)).collect(Collectors.toSet());
        if (result.size() > 1)
            throw new RuntimeException("Gramatică posibil ambiguă!");
        return result.size() == 1 ? result.iterator().next().getProduction() : null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Item it : this) {
            result.append(it.toString()).append("\n");
        }
        return "{" + result + "}";
    }
}
