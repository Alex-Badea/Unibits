import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.Scanner;

/**
 * Created by balex on 15.12.2016.
 */
public class Matrix {
    final int rowNo, colNo;
    final int[][] matrix;
    int k;

    ////
    public Matrix(String fileName) {
        int rowNo, colNo;
        int[][] matrix;

        try {
            Scanner in = new Scanner(new File(fileName));
            rowNo = in.nextInt();
            colNo = in.nextInt();

            matrix = new int[rowNo][colNo];
            for (int row = 0; row < matrix.length; row++)
                for (int col = 0; col < matrix[row].length; col++)
                    matrix[row][col] = in.nextInt();

            k = in.nextInt();
        } catch (Exception e) {
            rowNo = -1;
            colNo = -1;
            matrix = null;
        }

        this.rowNo = rowNo;
        this.colNo = colNo;
        this.matrix = matrix;
    }

    public void squareSubmatrices() {
        int[][] DPMatrix = generateDPMatrix();

        int maxSubmatrixSize = scanMatrix(DPMatrix, 0);

        int[] maxSubmatrixCoords = getMaxSubmatrixCoords(DPMatrix, maxSubmatrixSize);

        int kSubmatricesNo = getKSubmatricesNo(DPMatrix, k);

        System.out.println(maxSubmatrixSize);

        System.out.println(maxSubmatrixCoords[0] + " " + maxSubmatrixCoords[1]);

        System.out.println(kSubmatricesNo);
    }

    ////
    private int getKSubmatricesNo(int[][] DPMatrix, int k) {
        int counter = 0;

        for (int row = 0; row < DPMatrix.length; row++)
            for (int col = 0; col < DPMatrix[row].length; col++)
                if (DPMatrix[row][col] >= k)
                    counter += (DPMatrix[row][col] - k + 1);

        return counter;
    }

    private int[] getMaxSubmatrixCoords(int[][] DPMatrix, int maxSubmatrixSize) {
        for (int row = 0; row < DPMatrix.length; row++)
            for (int col = 0; col < DPMatrix[row].length; col++)
                if (DPMatrix[row][col] == maxSubmatrixSize)
                    return new int[]{row + 1, col + 1};

        return new int[]{-1,-1};
    }

    private int scanMatrix(int[][] DPMatrix, int currentRow) {
        if (currentRow < DPMatrix.length - 1) {
            int maxSubmatrixSize = scanMatrix(DPMatrix, currentRow + 1);

            for (int col = DPMatrix.length - 1; col >= 0; col--)
                if (DPMatrix[currentRow][col] != 0) {
                    DPMatrix[currentRow][col] = minInRange(DPMatrix, currentRow, col) + 1;
                    if (DPMatrix[currentRow][col] > maxSubmatrixSize)
                        maxSubmatrixSize = DPMatrix[currentRow][col];
                }

            return maxSubmatrixSize;
        } else {
            for (int col = DPMatrix.length - 1; col >= 0; col--)
                if (DPMatrix[currentRow][col] == 1)
                    return 1;
            return 0;
        }
    }

    private int minInRange(int[][] DPMatrix, int row, int col) {
        return Math.min(Math.min(DPMatrix[row][col + 1], DPMatrix[row + 1][col]), DPMatrix[row + 1][col + 1]);
    }

    private int[][] generateDPMatrix() {
        int[][] DPMatrix = new int[rowNo + 1][colNo + 1];
        for (int row = 0; row < DPMatrix.length - 1; row++)
            for (int col = 0; col < DPMatrix[row].length - 1; col++)
                DPMatrix[row][col] = -(matrix[row][col] - 1);

        return DPMatrix;
    }

    @Deprecated
    public void printMatrix(int[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++)
                System.out.print(matrix[row][col] + " ");
            System.out.println();
        }
    }
}
