package game;

import game.input.InputStream;
import game.input.Streamable;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by balex on 18.12.2016.
 */
public class Game {
    private final int[] table;
    private int leftmostIndex, rightmostIndex;
    private final int[][][] scoreMatrix;
    private final Selection[][] optimalSolutionMatrix;
    private final Player player1, player2;

    ////
    public Game(String fileName) {
        table = getTable(fileName);

        leftmostIndex = 0;
        rightmostIndex = table.length - 1;

        scoreMatrix = new int[table.length][table.length][2]; //Generat în scanTable
        optimalSolutionMatrix = generateOptimalSolutionMatrix();

        player1 = new Player(new Streamable<Selection>() {
            //TODO Se poate asigna ca InputStream orice metodă care asigură un flux constant de date de tip "Selection";
            //TODO În cazul de față, Jucătorului 1 îi este furnizată următoarea selecție optimă la fiecare mutare a acestuia:
            @Override
            public Selection getNext() {
                return fetchNextOptimalSelection(leftmostIndex, rightmostIndex);
            }
        });

        //player1 = new Player(InputStream.KEYBOARD);

        /*player2 = new Player(new Streamable<Selection>() {
            @Override
            public Selection getNext() {
                return fetchNextOptimalSelection(leftmostIndex, rightmostIndex);
            }
        });*/

        //player2 = new Player(InputStream.RANDOM);

        //player1 = new Player(InputStream.KEYBOARD);

        player2 = new Player(InputStream.KEYBOARD);
    }

    public void start() {
        int step = 1;

        final int player1ScoreIndex = 0;
        final int player2ScoreIndex = 1;

        int scoreDifference = scoreMatrix[table.length - 1][0][player1ScoreIndex] - scoreMatrix[table.length - 1][0][player2ScoreIndex];
        while (leftmostIndex <= rightmostIndex) {
            System.out.println("Pas " + step + ": ");
            System.out.println("Jucătorul " + (scoreDifference > 0 ? "1 " : "2 ") + "poate termina cu un avantaj de cel puțin " + Math.abs(scoreDifference) + " punct" + (Math.abs(scoreDifference) != 1 ? "e. " : ". ") + "(Dacă joacă optim)");

            printSubarray(leftmostIndex, rightmostIndex, table);
            if (leftmostIndex <= rightmostIndex) {
                System.out.println("Jucătorul 1: ");
                move(player1);
            }

            int currentRow = rightmostIndex - leftmostIndex;
            int currentCol = leftmostIndex;

            printSubarray(leftmostIndex, rightmostIndex, table);
            Selection player2Move = null;
            if (leftmostIndex <= rightmostIndex) {
                System.out.println("Jucătorul 2: ");
                player2Move = move(player2);
            }

            if (leftmostIndex <= rightmostIndex){
                Selection optimalMove = optimalSolutionMatrix[currentRow][currentCol];
                if (player2Move != optimalMove)
                    scoreDifference += 2 * Math.abs(scoreMatrix[currentRow - 1][currentCol][player1ScoreIndex] - scoreMatrix[currentRow - 1][currentCol + 1][player1ScoreIndex]);
            }

            step++;
            System.out.println();
        }

        System.out.println("Jucătorul 1: " + player1);
        System.out.println("Jucătorul 2: " + player2);

    }

    ////
    private Selection move(Player player) {
        if (player.move() == Selection.LEFT) {
            System.out.println("A selectat " + table[leftmostIndex]);
            player.addScore(table[leftmostIndex++]);
            return Selection.LEFT;
        } else {
            System.out.println("A selectat " + table[rightmostIndex]);
            player.addScore(table[rightmostIndex--]);
            return Selection.RIGHT;
        }
    }

    private Selection fetchNextOptimalSelection(int leftmostIndex, int rightmostIndex) {
        int currentRow = rightmostIndex - leftmostIndex;
        int currentCol = leftmostIndex;

        return optimalSolutionMatrix[currentRow][currentCol];
    }

    private Selection[][] generateOptimalSolutionMatrix() {
        return scanTable(scoreMatrix, table.length);
    }

    private Selection[][] scanTable(int[][][] DPMatrix, final int currentLength) {
        Selection[][] optimalSolutionMatrix;

        final int player1ScoreIndex = 0;
        final int player2ScoreIndex = 1;
        final int currentRow = currentLength - 1;
        final int currentSpacing = currentLength - 2;

        if (currentLength > 1) {
            optimalSolutionMatrix = scanTable(DPMatrix, currentLength - 1);

            int leftmostIndex, rightmostIndex;
            for (leftmostIndex = 0, rightmostIndex = leftmostIndex + currentSpacing + 1; rightmostIndex < DPMatrix.length; leftmostIndex++, rightmostIndex++) {
                int valueOnLeftEnd = table[leftmostIndex];
                int valueOnRightEnd = table[rightmostIndex];
                //Caz stânga:
                int prevPlayer1ScoreL = DPMatrix[currentRow - 1][leftmostIndex + 1][player1ScoreIndex];
                int prevPlayer2ScoreL = DPMatrix[currentRow - 1][leftmostIndex + 1][player2ScoreIndex];
                int currentPlayer1ScoreL = prevPlayer2ScoreL + valueOnLeftEnd;
                int currentPlayer2ScoreL = prevPlayer1ScoreL;
                //Caz deapta:
                int prevPlayer1ScoreR = DPMatrix[currentRow - 1][leftmostIndex][player1ScoreIndex];
                int prevPlayer2ScoreR = DPMatrix[currentRow - 1][leftmostIndex][player2ScoreIndex];
                int currentPlayer1ScoreR = prevPlayer2ScoreR + valueOnRightEnd;
                int currentPlayer2ScoreR = prevPlayer1ScoreR;

                if (currentPlayer1ScoreL - currentPlayer2ScoreL > currentPlayer1ScoreR - currentPlayer2ScoreR) {
                    DPMatrix[currentRow][leftmostIndex][player1ScoreIndex] = currentPlayer1ScoreL;
                    DPMatrix[currentRow][leftmostIndex][player2ScoreIndex] = currentPlayer2ScoreL;

                    optimalSolutionMatrix[currentRow][leftmostIndex] = Selection.LEFT;
                } else {
                    DPMatrix[currentRow][leftmostIndex][player1ScoreIndex] = currentPlayer1ScoreR;
                    DPMatrix[currentRow][leftmostIndex][player2ScoreIndex] = currentPlayer2ScoreR;

                    optimalSolutionMatrix[currentRow][leftmostIndex] = Selection.RIGHT;
                }

            }
        } else {
            optimalSolutionMatrix = new Selection[table.length][table.length];
            for (int col = 0; col < optimalSolutionMatrix[currentRow].length; col++) {
                optimalSolutionMatrix[currentRow][col] = Selection.LEFT;
                DPMatrix[currentRow][col][player1ScoreIndex] = table[col];
                DPMatrix[currentRow][col][player2ScoreIndex] = 0;
            }
        }

        return optimalSolutionMatrix;
    }

    private int[] getTable(String fileName) {
        ArrayList<Integer> tableAux;
        int[] table;

        try {
            Scanner in = new Scanner(new File(fileName));
            tableAux = new ArrayList<>();

            while (in.hasNext())
                tableAux.add(in.nextInt());

            table = new int[tableAux.size()];
            for (int i = 0; i < table.length; i++)
                table[i] = tableAux.get(i);

        } catch (Exception e) {
            table = null;
        }

        return table;
    }

    private void printSubarray(int startIndex, int endIndex, int[] array) {
        for (int i = startIndex; i <= endIndex; i++)
            System.out.print(array[i] + " ");
        System.out.println();
    }

    @Deprecated
    private void printMatrix(int[][][] DPMatrix) {
        for (int row = DPMatrix.length - 1; row >= 0; row--) {
            for (int col = 0; col < DPMatrix[row].length; col++) {
                if (row + col < DPMatrix.length)
                    System.out.print("(" + DPMatrix[row][col][0] + "," + DPMatrix[row][col][1] + ")");
            }
            System.out.println();
        }
    }

    @Deprecated
    private void printMatrix(Selection[][] DPMatrix) {
        for (int row = DPMatrix.length - 1; row >= 0; row--) {
            for (int col = 0; col < DPMatrix[row].length; col++) {
                if (row + col < DPMatrix.length)
                    System.out.print(DPMatrix[row][col] + " ");
            }
            System.out.println();
        }
    }
}
