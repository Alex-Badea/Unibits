package parser;

import parser.grammar.*;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private final ParsingTable parsingTable;

    ////
    public Parser(Grammar grammar) {
        this.parsingTable = new ParsingTable(grammar);
    }

    public void parse(String string) {
        var pointableTokens = new Pointable<Terminal>() {
            private final List<Terminal> tokens = tokenizeAndAddEof(string);
            private int pointerPos = 0;

            @Override
            public void shiftPointer() {
                pointerPos++;
            }

            @Override
            public Terminal getPointedElem() {
                return tokens.get(pointerPos);
            }

            @Override
            public boolean hasNext() {
                return pointerPos < tokens.size();
            }
        };
        ParsingStacks parsingStacks = new ParsingStacks();
        parsingStacks.pushState(parsingTable.getInitialState());

        while (pointableTokens.hasNext()) {
            parsingTable.get(parsingStacks.getCurrentState(), pointableTokens.getPointedElem()).accept(parsingStacks, pointableTokens);
            System.out.println(parsingStacks);
        }
    }

    private static List<Terminal> tokenizeAndAddEof(String string) {
        String[] stringTokens = string.split("[ ]+");
        return Stream.concat(Arrays.stream(stringTokens).map(Terminal::new),
                Stream.of(Terminal.EOF_TERMINAL)).collect(Collectors.toUnmodifiableList());
    }
}
