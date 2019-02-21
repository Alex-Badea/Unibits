import java.io.File;
import java.util.Scanner;

public class Main {
    static int[] v;

    public static void main(String[] args){
        v = generateArray("in.txt");

        System.out.print(getPeak());
    }

    private static int[] generateArray(String fileName) {
        try {
            int[] array;
            Scanner in = new Scanner((new File(fileName)));
            array = new int[in.nextInt()];
            for (int i = 0; in.hasNext(); i++) {
                array[i] = in.nextInt();
            }

            return array;
        } catch (Exception e) {return null;}
    }

    private static int getPeak(){
        return search(0,v.length-1);
    }

    private static int search(int leftmostIndex, int rightmostIndex){
        int middleIndex = (leftmostIndex+rightmostIndex)/2;
        int leftCell = v[middleIndex-1];
        int middleCell = v[middleIndex];
        int rightCell = v[middleIndex+1];

        if(leftCell < middleCell && middleCell < rightCell){ //caz de continuare 1
            return search(middleIndex,rightmostIndex);
        }
        else if(leftCell > middleCell && middleCell > rightCell){ //caz de continuare 2
            return search(leftmostIndex,middleIndex);
        }
        else if(leftCell < middleCell && middleCell > rightCell){ //caz de oprire
            return v[middleIndex];
        }
        return -1;
    }
}
