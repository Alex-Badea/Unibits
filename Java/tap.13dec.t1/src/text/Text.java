package text;

import java.io.File;
import java.util.*;

/**
 * Created by balex on 14.12.2016.
 */
public class Text {
    private final String[] words;

    ////
    public Text(String fileName) {
        String[] words;

        try {
            ArrayList<String> arrayList = new ArrayList<>();
            Scanner in = new Scanner(new File(fileName));
            while (in.hasNext())
                arrayList.add(in.next());

            words = new String[arrayList.size()];
            for (int i = 0; i < words.length; i++)
                words[i] = arrayList.get(i);

        } catch (Exception e) {
            words = null;
        }

        this.words = words;
    }

    public String[] getLongestWordChain() {
        ArrayList<String> longestWordChain = new ArrayList<>();

        HashMap<String, ChainedWord> chainMap = initializeChainMap(); //
        int[] succVector = new int[words.length];

        //TODO scanText returnează primul cuvânt din cel mai lung lanț și completează HashMap-urile cu datele aferente
        ChainedWord firstInChain = scanText(chainMap, succVector, 0);

        int currentIndex = firstInChain.getIndex();
        while (currentIndex != -1) {
            longestWordChain.add(words[currentIndex]);
            currentIndex = succVector[currentIndex];
        }

        String[] strings = new String[longestWordChain.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = longestWordChain.get(i);
        }

        return strings;
    }

    private HashMap<String, ChainedWord> initializeChainMap() {
        HashMap<String, ChainedWord> hashMap = new HashMap<>();

        for (String currentWord : words) {
            String startChars = "" + currentWord.charAt(0) + currentWord.charAt(1);
            String endChars = "" + currentWord.charAt(currentWord.length() - 2) + currentWord.charAt(currentWord.length() - 1);

            hashMap.put(startChars, new ChainedWord(null, 0,-1));
            hashMap.put(endChars, new ChainedWord(null, 0,-1));
        }

        return hashMap;
    }

    private ChainedWord scanText(HashMap<String, ChainedWord> chainMap, int[] succVector, int currentWordIndex) {
        String currentWord = words[currentWordIndex];
        String startChars = getStartChars(currentWord);
        String endChars = getEndChars(currentWord);

        if (currentWordIndex < words.length - 1) {
            ChainedWord chainedWord = scanText(chainMap, succVector, currentWordIndex + 1);

            //TODO Cuvântul ce se află curent la indexul startChars
            ChainedWord currentChainedWord = chainMap.get(startChars);
            //TODO Cuvântul ce înlocuiește (sau nu) cuvântul curent din hashmap
            ChainedWord replacerChainedWord = new ChainedWord(currentWord, chainMap.get(endChars).getPositionInChain() + 1, currentWordIndex);
            if (replacerChainedWord.getPositionInChain() > currentChainedWord.getPositionInChain()) {
                chainMap.put(startChars, replacerChainedWord);
            }

            int succIndex = chainMap.get(endChars).getIndex();
            succVector[currentWordIndex] = succIndex;

            return chainedWord.getPositionInChain() > chainMap.get(startChars).getPositionInChain() ? chainedWord : chainMap.get(startChars);
        } else {
            ChainedWord chainedWord = new ChainedWord(currentWord, 1, currentWordIndex);

            chainMap.put(startChars, chainedWord);
            succVector[currentWordIndex] = -1;

            return chainedWord;
        }
    }

    private String getStartChars(String string) {
        return "" + string.charAt(0) + string.charAt(1);
    }

    private String getEndChars(String string) {
        return "" + string.charAt(string.length() - 2) + string.charAt(string.length() - 1);
    }

    @Deprecated
    public void printArray(int[] array){
        for(int i =0;i<array.length;i++)
            System.out.println(i+": "+array[i]);
        System.out.println();
    }
    @Deprecated
    public static void printMap(Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }
}
