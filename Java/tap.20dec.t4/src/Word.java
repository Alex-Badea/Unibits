import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by balex on 15.12.2016.
 */
public class Word {
    private final String word;

    ////
    public Word(String fileName) {
        String word;

        try {
            Scanner in = new Scanner(new File(fileName));
            word = in.next();
        } catch (Exception e) {
            word = null;
        }

        this.word = word;
    }

    public void palindromes() { //O(n^2)
        int[][] palindromeMatrix = new int[word.length()][word.length()];

        //TODO Completează palindromeMatrix cu datele aferente și returnează numărul de palindromuri:
        int palindromesNo = scanWordForPalindromes(palindromeMatrix, word.length()); //O(n^2)

        System.out.println(palindromesNo);

        //TODO Matricea care furnizează partiționarea optimă în palindromuri:
        double[][] palPartArray = new double[word.length()][2];

        scanWordForPalPart(palPartArray, word.length(), palindromeMatrix);          //O(n^2)

        String[] partitions = getPartitions(palPartArray);              //O(n^2)

        for (String s:partitions)                                   //O(n)
            System.out.println(s);
    }

    ////
    private String[] getPartitions(final double[][] _palPartArray) { //O(n^2)
        ArrayDeque<String> partitionsAux = new ArrayDeque<>();
        int separatorIndex = 0;
        int scoreIndex = 1;

        int startIndex;
        int endIndex = word.length() - 1;
        do{                                 //O(n^2)
            startIndex = (int)Math.ceil(_palPartArray[endIndex][separatorIndex]);
            partitionsAux.addFirst(getSubstring(word,startIndex,endIndex));            //O(n)
            endIndex = startIndex - 1;
        }while(startIndex != 0);

        String[] partitions = new String[partitionsAux.size()];
        for(int i=0;i<partitions.length;i++)                    //O(n)
            partitions[i] = partitionsAux.removeFirst();

        return partitions;
    }

    private String getSubstring(String string, int leftIndex, int rightIndex) { //O(n)
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rightIndex - leftIndex + 1; i++)
            stringBuilder.append(string.charAt(i + leftIndex));
        return stringBuilder.toString();
    }

    private int scanWordForPalindromes(int[][] palindromeMatrix, int currentLength) { //O(n^2)

        //TODO Apel recursiv:
        if (currentLength > 1) {
            int previousCounter = scanWordForPalindromes(palindromeMatrix, currentLength - 1); //O(n^2)
            int currentCounter = 0;

            int spacing = currentLength - 2;
            int leftmostLetterIndex, rightmostLetterIndex;
            for (leftmostLetterIndex = 0, rightmostLetterIndex = spacing + 1; rightmostLetterIndex < word.length(); leftmostLetterIndex++, rightmostLetterIndex++) { //O(n)
                char leftmostLetter = word.charAt(leftmostLetterIndex);
                char rightmostLetter = word.charAt(rightmostLetterIndex);
                boolean wordBetweenIsPalindrome;
                //TODO Când lungimea cuvântului este 2, nu există vreun cuvânt între literele extreme:
                wordBetweenIsPalindrome = currentLength == 2 || palindromeMatrix[currentLength - 3][leftmostLetterIndex + 1] == 1;

                if ((leftmostLetter == rightmostLetter) && wordBetweenIsPalindrome) {
                    palindromeMatrix[currentLength - 1][leftmostLetterIndex] = 1;
                    currentCounter++;
                }
            }

            return currentCounter + previousCounter;
        }
        //TODO Caz de oprire:
        else {
            //TODO Toate cuvintele de lungime 1 sunt palindromuri triviale:
            for (int col = 0; col < palindromeMatrix[currentLength - 1].length; col++)
                palindromeMatrix[currentLength - 1][col] = 1;

            return word.length();
        }

    }

    private void scanWordForPalPart(double[][] palPartArray, int currentLength, final int[][] _palindromeMatrix) { //O(n^2)
        int separatorIndex = 0;
        int scoreIndex = 1;

        if (currentLength > 1) {
            scanWordForPalPart(palPartArray, currentLength - 1, _palindromeMatrix); //O(n^2)

            int score = word.length() + 1;
            for (double separator = currentLength - 1.5; separator >= -0.5; separator--) { //O(n)
                int wordBeforeSeparatorStartIndex = 0;
                int wordBeforeSeparatorLength = (int)Math.ceil(separator);
                int wordAfterSeparatorStartIndex = (int)Math.ceil(separator);
                int wordAfterSeparatorLength = currentLength - wordAfterSeparatorStartIndex;

                boolean wordAfterSeparatorIsPalindrome = _palindromeMatrix[wordAfterSeparatorLength - 1][wordAfterSeparatorStartIndex] == 1;
                double wordAfterSeparatorScore = wordAfterSeparatorIsPalindrome? 1 : wordAfterSeparatorLength;
                double wordBeforeSeparatorScore = wordBeforeSeparatorLength != 0? palPartArray[wordBeforeSeparatorLength - 1][scoreIndex] : 0;

                int potentialScore = (int)(wordBeforeSeparatorScore + wordAfterSeparatorScore);
                if (potentialScore < score){
                    score = potentialScore;
                    palPartArray[currentLength - 1][separatorIndex] = separator;
                    palPartArray[currentLength - 1][scoreIndex] = score;
                }
            }

        } else {
            palPartArray[currentLength - 1][separatorIndex] = -0.5;
            palPartArray[currentLength - 1][scoreIndex] = 1;
        }

    }

    @Deprecated
    public void printMatrix(int[][] matrix) {
        for (int row = matrix.length - 1; row >= 0; row--) {
            for (int col = 0; col < matrix[row].length; col++)
                if (col + row == matrix.length)
                    System.out.print("X ");
                else
                    System.out.print(matrix[row][col] + " ");
            System.out.println(row + 1);
        }
        for (int i = 0; i < word.length(); i++)
            System.out.print(word.charAt(i) + " ");
    }

    @Deprecated
    public void printMatrix(double[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++)
                System.out.print(matrix[row][col] + " ");
            System.out.println();
        }
    }
}
