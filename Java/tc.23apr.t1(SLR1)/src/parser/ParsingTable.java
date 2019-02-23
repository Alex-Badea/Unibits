package parser;

import parser.grammar.*;
import misc.CustomIndexMatrix;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParsingTable extends CustomIndexMatrix<State, Terminal, BiConsumer<Actionable, Pointable>> {
    private final Grammar grammar;
    private final State initialState;

    ////
    ParsingTable(Grammar grammar) {
        this.grammar = grammar;
        State bruteInitialState = new State();
        bruteInitialState.add(createInitialItem(grammar.getStartNonterminal()));
        initialState = kernelClosure(bruteInitialState);

        // Se populează automatul:
        var automaton = new CustomIndexMatrix<State, Symbol, State>();
        var discoveredStates = new ArrayDeque<State>();
        discoveredStates.add(initialState);
        while (!discoveredStates.isEmpty()) {
            var currentState = discoveredStates.remove();
            var allSymbols = Stream.concat(grammar.getAllSymbolsSet().stream(), Stream.of(Terminal.EOF_TERMINAL)).
                    collect(Collectors.toUnmodifiableSet());
            for (var sym : allSymbols) {
                var newState = kernelClosure(currentState.consume(sym));
                if (!automaton.contains(newState))
                    discoveredStates.add(newState);
                automaton.set(currentState, sym, newState);
            }
        }
        // Se populează this
        for (final State currentState : automaton.keySet()) {
            var allTerminals = Stream.concat(grammar.getAllSymbolsSet().stream(), Stream.of(Terminal.EOF_TERMINAL)).
                    filter(Symbol::isTerminal).map(Terminal.class::cast).collect(Collectors.toUnmodifiableSet());
            for (final var transitionTerminal : allTerminals) {
                final var transitionState = automaton.get(currentState, transitionTerminal);
                boolean shiftable = !transitionState.isEmpty();
                Production reduction = currentState.getReductionUponLookahead(transitionTerminal);
                boolean reducible = reduction != null;

                if (shiftable && reducible) {
                    throw new RuntimeException("Gramatică ambiguă!\n" + currentState + " reductibilă și shift-abilă sub " + transitionTerminal + "\n" + currentState.getReductionUponLookahead(transitionTerminal));
                } else if (shiftable) { this.set(currentState, transitionTerminal, (actionable, pointable) -> {
                        actionable.shift(transitionState, transitionTerminal);
                        pointable.shiftPointer();
                    });
                } else if (reducible) {
                    var reduceGoto = ((BiConsumer<Actionable, Pointable>) (actionable, pointable) ->
                            actionable.reduce(reduction)).andThen(((actionable, pointable) ->
                            actionable.goTo(automaton.get(actionable.getCurrentState(), reduction.getProducer()))));
                    this.set(currentState, transitionTerminal, reduceGoto);
                } else {
                    this.set(currentState, transitionTerminal, (actionable, pointable) -> actionable.error(currentState,
                            transitionTerminal, currentState.getConsumables()));
                }
            }
        }
    }

    State getInitialState() {
        return initialState;
    }

    private Item createInitialItem(Nonterminal startNonterminal) {
        return new Item(new Production(new Nonterminal("AUG_START"), new SequenceOfSymbols(startNonterminal, Terminal.EOF_TERMINAL)),
                Terminal.EOF_TERMINAL);
    }

    private State kernelClosure(State kernel) {
        var result = new State();
        for (Item it : kernel) {
            result.addAll(closure(new Stack<>(), it));
        }
        return result;
    }

    private State closure(Stack<Item> visitedItems, Item item) {
        if (item.isEpsilonItem())
            return new State(Set.of(item));
        if (item.getLook() == null)
            return new State(Set.of(item));
        if (item.getLook().isTerminal())
            return new State(Set.of(item));

        var result = new State();
        result.add(item);
        ListOfProductions prods = grammar.getListOfProductions((Nonterminal) item.getLook());
        for (var prod : prods) {
            SequenceOfSymbols bruteLookahead = new SequenceOfSymbols(item.getAllAfterLook());
            bruteLookahead.add(item.getLookahead());
            Set<SequenceOfSymbols> LOOKAHEADS_UNADAPTED = grammar.bruteFirst(new Stack<>(), bruteLookahead);
            // TODO MODIFICĂ COMPLET ARHITECTURA GRAMATICII!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Set<Terminal> LOOKAHEADS_ADAPTED = LOOKAHEADS_UNADAPTED.stream().
                    map(symbols -> (Terminal) symbols.get(0)).collect(Collectors.toSet());
            State items = Item.assembleMultiple(prod, LOOKAHEADS_ADAPTED);
            State closure = closure(visitedItems, items);
            result.addAll(closure);
        }
        return result;
    }

    // ↕ CICLIC RECURSIVE
    private State closure(Stack<Item> visitedItems, State state) {
        var result = new State();
        for (var it : state) {
            if (visitedItems.contains(it))
                continue;

            visitedItems.push(it);
            State closure = closure(visitedItems, it);
            result.addAll(closure);
            visitedItems.pop();
        }
        return result;
    }

    @Deprecated
    public void $TEST$() {
        System.out.println(this);
    }
}