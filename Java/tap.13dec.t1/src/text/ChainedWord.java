package text;

/**
 * Created by balex on 13.12.2016.
 */
public class ChainedWord {
    private final String word;
    private int positionInChain;
    private final int index;

    ChainedWord(String word, int positionInChain, final int index){
        this.word = word;
        this.positionInChain = positionInChain;
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public int getPositionInChain() {
        return positionInChain;
    }

    public int getIndex(){
        return index;
    }

    @Override
    public String toString(){
        return word+": "+positionInChain;
    }
}
