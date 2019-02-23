package misc;

@Deprecated
public class $LEGACYTRASH$ {
    //Grammar::
    /**private Set<SequenceOfSymbols> first(Stack<Nonterminal> visitedNonterminals, ListOfProductions productions) {
     Set<SequenceOfSymbols> terminalsSet = new LinkedHashSet<>();
     // Preprocesăm în prealabil fiecare ε-producție și îi inițializăm mulțimea first cu {ε}
     if (anyProductionYieldsEpsilon(productions.getProducer())) {
     terminalsSet.add(SequenceOfSymbols.EPSILON_SEQUENCE);
     }
     for (Production currentProduction : productions) {
     // Nu lăsăm ε-producțiile să fie procesate de first; de ele ne-am ocupat în stagiul anterior, înainte de
     // execuția acestei bucle.
     if(!currentProduction.isEpsilonProduction())
     terminalsSet.addAll(first(visitedNonterminals, currentProduction));
     }
     return terminalsSet;
     }

     private Set<SequenceOfSymbols> first(Stack<Nonterminal> visitedNonterminals, Production production) {
     Set<SequenceOfSymbols> terminalsSet = new LinkedHashSet<>();
     for (Symbol currentSymbol : production.getSymbols()) {
     Set<SequenceOfSymbols> result = first(visitedNonterminals, currentSymbol);
     result.remove(SequenceOfSymbols.EPSILON_SEQUENCE);
     terminalsSet.addAll(result);

     // Când e nonterminal și NU are ε-Producție...
     if (!currentSymbol.isTerminal() && !anyProductionYieldsEpsilon((Nonterminal) currentSymbol))
     break;
     // ...sau când e terminal...
     else if(currentSymbol.isTerminal())
     break;
     // ...intrerupe execuția; am găsit mulțimea first a producției curente
     }
     return terminalsSet;
     }

     private Set<SequenceOfSymbols> first(Stack<Nonterminal> visitedNonterminals, Symbol symbol) {
     Set<SequenceOfSymbols> terminalsSet = new LinkedHashSet<>();
     if (symbol.isTerminal()) {
     terminalsSet.add(new SequenceOfSymbols(symbol));
     return terminalsSet;
     } else if (!symbol.isTerminal()) {
     if (visitedNonterminals.contains((Nonterminal) symbol)) {
     System.err.println("Recursie stângă detectată! " + new LeftRecursiveGrammarException(visitedNonterminals).getMessage());
     return Collections.emptySet();
     }
     visitedNonterminals.push((Nonterminal) symbol);
     return first(visitedNonterminals, getListOfProductions((Nonterminal) symbol));
     } else throw new AssertionError(this.getClass().getCanonicalName() + "::" + new Object(){}.getClass().getEnclosingMethod().getName() + ": Simbol invalid: " + symbol);
     }*/

    //ParsingTable::
    /**    private void fetchAutomatonAndIndexedStates(final CustomIndexMatrix<Integer, Integer, Symbol> out_automaton, final Map<Integer, State> out_indexedStates) {
        @BijectiveMap var stateAliases = new LinkedHashMap<State, Integer>();
        stateAliases.put(initialState, 0);
        var visitedStates = new ArrayDeque<State>();
        visitedStates.push(initialState);

        for (int currentStateNo = 0, discoveredStatesNo = 1; !visitedStates.isEmpty(); currentStateNo++) {
            var currentState = visitedStates.removeLast();
            for (var sym : grammar.getAllSymbolsSet()) {
                var newState = kernelClosure(currentState.consume(sym));
                if (stateAliases.containsKey(newState)) {
                    out_automaton.set(currentStateNo, stateAliases.get(newState), sym);
                } else if (!newState.isEmpty() && !stateAliases.containsKey(newState)) {
                    stateAliases.put(newState, discoveredStatesNo);
                    visitedStates.push(newState);
                    out_automaton.set(currentStateNo, discoveredStatesNo++, sym);
                }
            }
        }
        out_indexedStates.putAll(Utils.inverse(stateAliases));
    }*/
}
