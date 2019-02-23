package parser.grammar;

import java.util.ArrayList;
/**
 * Created by balex on 23.04.2018.
 */
public class ListOfProductions extends ArrayList<Production> {
    private final Nonterminal producer;
    ////
    ListOfProductions(Nonterminal producer) {
        super();
        this.producer = producer;
    }

    Nonterminal getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Production p : this) {
            stringBuilder.append(p.toString()).append(" | ");
        }
        stringBuilder.append("\b\b  ");
        return stringBuilder.toString();
    }
}
