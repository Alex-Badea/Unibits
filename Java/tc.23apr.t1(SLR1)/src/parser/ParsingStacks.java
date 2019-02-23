package parser;

import parser.grammar.Production;
import parser.grammar.Symbol;
import parser.grammar.Terminal;

import java.util.Stack;

public class ParsingStacks implements Actionable {
    private final Stack<State> states;
    private final Stack<Symbol> symbols;

    ////
    ParsingStacks() {
        this.states = new Stack<>();
        this.symbols = new Stack<>();
    }

    void pushState(State state) {
        states.push(state);
    }

    @Override
    public State getCurrentState() {
        return states.peek();
    }

    @Override
    public void reduce(Production reduction) {
        var reductionStack = new Stack<Symbol>();
        reduction.getSymbols().forEach(reductionStack::push);
        while (!reductionStack.isEmpty()) {
            assert reductionStack.peek().equals(symbols.peek());
            reductionStack.pop();
            states.pop();
            symbols.pop();
        }
        symbols.push(reduction.getProducer());
    }

    @Override
    public void goTo(State where) {
        states.push(where);
    }

    @Override
    public void shift(State where, Terminal with) {
        states.push(where);
        symbols.push(with);
    }

    @Override
    public String toString() {
        return "States:\n" + states + "\nSymbols:\n" + symbols;
    }
}
