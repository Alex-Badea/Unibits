package parser.grammar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import misc.BijectiveMap;

/**
 * Created by balex on 23.04.2018.
 */
public final class Grammar {
    static final String EPSILON_NOTATION = "ε";
    static final String EOF_NOTATION = "$";
    private final Nonterminal startNonterminal;
    private final @BijectiveMap Map<String, Symbol> allSymbols;
    private final Map<Nonterminal, ListOfProductions> productionsMap;

    ////
    public Grammar(File specFile) throws FileNotFoundException {
        Map<String, Symbol> allSymbols = new LinkedHashMap<>();
        String startNonterminalName = preprocessAndGetStartNonterminalName(specFile, allSymbols);
        startNonterminal = new Nonterminal(startNonterminalName);
        this.allSymbols = allSymbols;

        Scanner lineScanner = new Scanner(specFile);
        Map<Nonterminal, ListOfProductions> productionsMap = new LinkedHashMap<>() {
            @Override
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry e : this.entrySet()) {
                    stringBuilder.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
                }
                return stringBuilder.toString();
            }
        };
        while (lineScanner.hasNext()) {
            Scanner ruleScanner = new Scanner(lineScanner.nextLine());
            ruleScanner.useDelimiter("[ ]*->[ ]*");

            // Creează leftProducer
            Nonterminal leftProducer = new Nonterminal(ruleScanner.next());

            Scanner productionScanner = new Scanner(ruleScanner.next());
            productionScanner.useDelimiter("[ ]*\\|[ ]*");
            // Populează rightProductions
            ListOfProductions rightProductions = new ListOfProductions(leftProducer);
            while (productionScanner.hasNext()) {
                String currentProductionString = productionScanner.next();
                // Verific LA ÎNCEPUT dacă este ε-producție
                if (EPSILON_NOTATION.equals(new Scanner(currentProductionString).next())) {
                    rightProductions.add(Production.createEpsilonProduction(leftProducer));
                    break;
                }

                Scanner symbolScanner = new Scanner(currentProductionString);
                // Populează symbols
                List<Symbol> symbols = new ArrayList<>();
                while (symbolScanner.hasNext()) {
                    symbols.add(allSymbols.get(symbolScanner.next()));
                }

                rightProductions.add(new Production(leftProducer, new SequenceOfSymbols(symbols)));
            }

            productionsMap.put(leftProducer, rightProductions);
        }
        this.productionsMap = productionsMap;
    }

    public Nonterminal getStartNonterminal() {
        return startNonterminal;
    }

    public Set<Symbol> getAllSymbolsSet() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(allSymbols.values()));
    }

    private String preprocessAndGetStartNonterminalName(File specFile, final Map<String, Symbol> out_allSymbols) throws FileNotFoundException /*DE CE ARUNCĂ FILENOTFOUNDEXCEPTION CÂND DEJA ÎI TRIMIT UN FIȘIER VALID CA PARAMETRU?!!?!?!?!?!?!?!?!?!?!!?!??!*/ {
        Set<String> allSymbolNames = new LinkedHashSet<>();
        Set<String> nonterminalNames = new LinkedHashSet<>();
        Set<String> terminalNames;

        Scanner nonterminalScanner = new Scanner(specFile);
        String startNonterminalName = nonterminalScanner.next();
        nonterminalNames.add(startNonterminalName);
        nonterminalScanner.nextLine();
        while (nonterminalScanner.hasNext()) {
            String currentNonterminalName = nonterminalScanner.next();
            if (nonterminalNames.contains(currentNonterminalName)) {
                // Verific dacă specificația este malformată (s-a citit deja lista de producții a lui leftProducer)
                throw new RuntimeException("Specificație malformată: " + currentNonterminalName + " s-a citit deja.");
            }
            nonterminalNames.add(currentNonterminalName);
            nonterminalScanner.nextLine();
        }

        Scanner allScanner = new Scanner(specFile);
        allScanner.useDelimiter("\\s*(->|\\||\\s+)\\s*");
        while (allScanner.hasNext()) {
            String currentSymbol = allScanner.next();
            if (!EPSILON_NOTATION.equals(currentSymbol))
                allSymbolNames.add(currentSymbol);
        }

        terminalNames = new LinkedHashSet<>(allSymbolNames);
        terminalNames.removeAll(nonterminalNames);

        for (String nonterminalName : nonterminalNames) {
            out_allSymbols.put(nonterminalName, new Nonterminal(nonterminalName));
        }
        for (String terminalName : terminalNames) {
            out_allSymbols.put(terminalName, new Terminal(terminalName));
        }

        return startNonterminalName;
    }

    /**
     * @deprecated Nu e necesar pentru implementarea LR(1);
     */
    @Deprecated
    public Map<Nonterminal, Set<SequenceOfSymbols>> first() {
        Map<Nonterminal, Set<SequenceOfSymbols>> terminalsLists = new LinkedHashMap<>();
        for (Map.Entry<Nonterminal, ListOfProductions> currentEntry : productionsMap.entrySet()) {
            Nonterminal currentProducer = currentEntry.getKey();
            ListOfProductions currentProductions = currentEntry.getValue();
            assert currentProducer.equals(currentProductions.getProducer());

            terminalsLists.put(currentProductions.getProducer(), first(currentProductions.getProducer()));
        }
        return terminalsLists;
    }

    private Set<SequenceOfSymbols> first(Nonterminal nt) {
        Set<SequenceOfSymbols> nonterminalsSet = bruteFirst(new Stack<>(), getListOfProductions(nt));
        if (anyProductionYieldsEpsilon(nt))
            nonterminalsSet.add(SequenceOfSymbols.EPSILON_SEQUENCE);
        return nonterminalsSet;
    }

    // Returnează o listă "brută" de terminale, fără ε
    private Set<SequenceOfSymbols> bruteFirst(Stack<Nonterminal> visitedNonterminals, ListOfProductions productions) {
        Set<SequenceOfSymbols> terminalsSet = new LinkedHashSet<>();
        for (Production prod : productions) {
            terminalsSet.addAll(bruteFirst(visitedNonterminals, prod.getSymbols()));
        }
        return terminalsSet;
    }

    // ↕ CICLIC RECURSIVE
    public Set<SequenceOfSymbols> bruteFirst(Stack<Nonterminal> visitedNonterminals, SequenceOfSymbols symbols) {
        if (symbols.isEpsilonSequence())
            return Collections.emptySet();

        Set<SequenceOfSymbols> terminalsSet = new LinkedHashSet<>();
        for (Symbol sym : symbols) {
            if (sym.isTerminal()) {
                terminalsSet.add(new SequenceOfSymbols(sym));
                return terminalsSet;
            }
            else if (!sym.isTerminal()) {
                if (visitedNonterminals.contains((Nonterminal) sym)) {
                    return Collections.emptySet();
                }

                visitedNonterminals.push((Nonterminal) sym);
                terminalsSet.addAll(bruteFirst(visitedNonterminals, getListOfProductions((Nonterminal) sym)));
                visitedNonterminals.pop();

                if (!anyProductionYieldsEpsilon((Nonterminal) sym))
                    break;
            } else throw new AssertionError(this.getClass().getCanonicalName() + "::" + getClass().getEnclosingMethod().getName() + ": Simbol invalid: " + sym);
        }
        return terminalsSet;
    }

    private boolean anyProductionYieldsEpsilon(Nonterminal nt) {
        return yieldsEpsilon(new Stack<>(), getListOfProductions(nt));
    }

    private boolean yieldsEpsilon(Stack<Nonterminal> visitedNonterminals, ListOfProductions productions) {
        for (Production prod : productions)
            if (yieldsEpsilon(visitedNonterminals, prod.getSymbols()))
                return true;
        return false;
    }

    // ↕ CICLIC RECURSIVE
    private boolean yieldsEpsilon(Stack<Nonterminal> visitedNonterminals, SequenceOfSymbols symbols) {
        if (symbols.containsAnyTerminal())
            return false;
        if (symbols.isEpsilonSequence())
            return true;

        boolean flag = true;
        for (Nonterminal nt : symbols.getNonterminalsSet()) {
            if (visitedNonterminals.contains(nt))
                return false;

            visitedNonterminals.push(nt);
            flag = flag && yieldsEpsilon(visitedNonterminals, getListOfProductions(nt));
            visitedNonterminals.pop();
        }
        return flag;
    }

    /**
     * @deprecated Nu e necesar pentru implementarea LR(1);
     */
    @Deprecated
    public Map<Nonterminal, Set<SequenceOfSymbols>> follow() {
        var bruteFollowSets = bruteFollow();
        var resultingFollowSets = new LinkedHashMap<Nonterminal, Set<SequenceOfSymbols>>();
        for (var currentProductions : productionsMap.values()) {
            Nonterminal currentProducer = currentProductions.getProducer();
            Set<SequenceOfSymbols> currentBruteFollowSet = bruteFollowSets.get(currentProducer);
            var currentResultingFollowSet = new LinkedHashSet<SequenceOfSymbols>();
            for (SequenceOfSymbols currentSequence : currentBruteFollowSet) {
                currentResultingFollowSet.addAll(bruteFirst(new Stack<>(), currentSequence));
            }
            resultingFollowSets.put(currentProducer, currentResultingFollowSet);
        }
        return resultingFollowSets;
    }

    private Map<Nonterminal, Set<SequenceOfSymbols>> bruteFollow() {
        var resultingSets = new LinkedHashMap<Nonterminal, Set<SequenceOfSymbols>>();
        for (var currentProductions : productionsMap.values()) {
            var currentProducer = currentProductions.getProducer();
            resultingSets.put(currentProducer, bruteFollow(new Stack<>(), currentProducer));
        }
        return resultingSets;
    }

    // Returnează o mulțime "brută" de simboluri (terminale + nonterminale) potențial apartenente la mulțimea reală follow
    private Set<SequenceOfSymbols> bruteFollow(Stack<Nonterminal> visitedNonterminals, Nonterminal nt) {
        if (visitedNonterminals.contains(nt))
            return Collections.emptySet();
        var resultingSet = new LinkedHashSet<SequenceOfSymbols>();
        if (nt.equals(startNonterminal))
            resultingSet.add(new SequenceOfSymbols(Terminal.EOF_TERMINAL));
        var allProductionsSet = getProductionsAsSet();
        for (var currentProduction : allProductionsSet) {
            var rightSequencesSet = currentProduction.getSequencesToRightOf(nt);
            if (rightSequencesSet != null) {
                visitedNonterminals.push(nt);
                var left = currentProduction.getSequencesToRightOf(nt);
                var right = bruteFollow(visitedNonterminals, currentProduction.getProducer());
                var toAdd = cartesianConcat(left, right);
                resultingSet.addAll(toAdd);
                visitedNonterminals.pop();
            }
        }
        return resultingSet;
    }

    private Set<Production> getProductionsAsSet() {
        var resultingSet = new LinkedHashSet<Production>();
        for (var currentProductions : productionsMap.values())
            resultingSet.addAll(currentProductions);
        return resultingSet;
    }

    private static Set<SequenceOfSymbols> cartesianConcat(Set<SequenceOfSymbols> fst, Set<SequenceOfSymbols> snd) {
        if (fst.size() == 0)
            return snd;
        if (snd.size() == 0)
            return fst;
        var result = new LinkedHashSet<SequenceOfSymbols>();
        for (final var s : snd) {
            result.addAll(fst.stream().map(e -> {
                var aux = new SequenceOfSymbols(e);
                aux.addAll(s);
                return aux;
            }).collect(Collectors.toSet()));
        }
        return result;
    }

    public ListOfProductions getListOfProductions(Nonterminal nt) {
        return productionsMap.get(nt);
    }

    @Override
    public String toString() {
        return "CFG {" +
                "\nallSymbols=" + allSymbols.toString().replace("=", ": ") +
                "\nrules=\n" + productionsMap +
                '}';
    }
}
