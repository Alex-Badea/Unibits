import java.io.File;
import java.util.Scanner;

/**
 * Created by balex on 29.11.2016.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(new File("in.txt"));
        int n,t;
        n=in.nextInt();
        t=in.nextInt();

        for(int i=0;i<t;i++){
            int y = in.nextInt();
            int x = in.nextInt();
            System.out.println(getValue(n,x,y));
        }

        test(4);                                     //metoda de testare (afișează toată matricea pt un n dat)

    }

    private static int getValue(int n, int x, int y){
        return (int)select(n,Math.pow(2,n),x,y);
    }

    private static double select(int n, double currentMatrixSize, final double x, final double y){
        if(n==1){
            if(x==1 && y==1)
                return 1;
            if(x==1 && y==2)
                return 3;
            if(x==2 && y==1)
                return 2;
            if(x==2 && y==2)
                return 4;
        }
        else {
            if ((x >= 1 && x <= currentMatrixSize /2) && (y >= 1 && y <= currentMatrixSize /2)) {
                return select(n - 1, currentMatrixSize /2, x, y);                                                     //TODO: SELECT CADRAN STÂNGA-SUS
            }
            else if ((x >= 1 && x <= currentMatrixSize /2) && (y >= currentMatrixSize /2 + 1 && y <= currentMatrixSize)) {
                return 0.5 * currentMatrixSize * currentMatrixSize + select(n - 1, currentMatrixSize /2,  x, y - currentMatrixSize /2); //TODO: SELECT CADRAN STÂNGA-JOS
            }
            else if ((x >= currentMatrixSize /2 + 1) && (x <= currentMatrixSize) && (y >= 1 && y <= currentMatrixSize /2)) {
                return 0.25 * currentMatrixSize * currentMatrixSize + select(n - 1, currentMatrixSize /2,  x - currentMatrixSize /2, y);//TODO: SELECT CADRAN DREAPTA-SUS
            }
            else if ((x >= currentMatrixSize /2 + 1) && (x <= currentMatrixSize) && (y >= currentMatrixSize /2 + 1 && y <= currentMatrixSize)) {
                return 0.75 * currentMatrixSize * currentMatrixSize + select(n - 1, currentMatrixSize /2,  x - currentMatrixSize /2, y - currentMatrixSize /2);//TODO: SELECT CADRAN DREAPTA-JOS
            }
        }
        return -1;
    }

    @Deprecated
    private static void test(int n){
        for(int i=1;i<=Math.pow(2,n);i++){
            for(int j=1;j<=Math.pow(2,n);j++)
                System.out.print(getValue(n,j,i)+" ");
            System.out.println();
        }
    }
}
