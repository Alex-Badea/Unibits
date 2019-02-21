package table;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by balex on 14.12.2016.
 */
public class Table {
    private final int rowNo, colNo;
    private final int[][] table;
    ////
    public Table(String fileName){
        int rowNo, colNo;
        int[][] table;

        try{
            Scanner in = new Scanner(new File(fileName));
            rowNo = in.nextInt();
            colNo = in.nextInt();

            table = new int[rowNo][colNo];
            for(int row=0;row<table.length;row++)
                for(int col=0;col<table[row].length;col++)
                    table[row][col] = in.nextInt();
        }
        catch (Exception e){rowNo=-1;colNo=-1;table=null;}

        this.rowNo = rowNo;
        this.colNo = colNo;
        this.table = table;
    }

    public Coordinate[] getOptimalRoute(){
        ArrayList<Coordinate> optimalRoute = new ArrayList<>();
        int[][] routeTable = scanTable(0);

        boolean isUnique = true;

        int currentRow = startCell(routeTable).getY();
        for(int row=0;row<routeTable.length;row++)
            for(int col=0;col<routeTable[row].length;col++){

            }

        return null;
    }

    private Coordinate startCell(int[][] routeTable){

        int val = -1;
        int startRow = -1;
        for(int row=0;row<routeTable.length;row++)
            if(routeTable[row][0] > val) {
                val = routeTable[row][0];
                startRow = startRow;
            }

        return new Coordinate(0, startRow);
    }

    private int[][] scanTable(int currentCol){
        if(currentCol < colNo-1){
            int[][] routeTable = scanTable(currentCol+1);

            for(int row=0;row<routeTable.length;row++)
                routeTable[row][currentCol] = table[row][currentCol] + maxInRange(routeTable, row, currentCol);
            return routeTable;
        }
        else{
            int[][] routeTable = new int[rowNo][colNo];
            for(int row=0;row<routeTable.length;row++)
                routeTable[row][currentCol] = table[row][currentCol];
            return routeTable;
        }
    }

    private int maxInRange(int[][] table, int row, int col){
        return Math.max(Math.max(get(table, row-1, col+1),get(table, row, col+1)), get(table, row+1, col+1));
    }

    private int get(int[][] table, int row, int col){
        if(row < 0 || row > rowNo-1 || col < 0 || col > colNo-1)
            return 0;
        else return table[row][col];
    }

    @Deprecated
    public void printTable(int[][] table){
        for(int row=0;row<table.length;row++) {
            for (int col=0;col<table[row].length;col++)
                System.out.print(table[row][col] + " ");
            System.out.println();
        }
    }
}
