import java.io.File;
import java.util.Scanner;

/**
 * Created by balex on 27.11.2016.
 */
public class Main {
    enum Half{LEFT,RIGHT}

    static int inv=0;

    public static void main(String[] args) {
        //int[] v = generateArray("in.txt");

        int[] v = {4,8,11,3,5,12};

        inversions(v);

        System.out.print(inv);
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
        } catch (Exception e) {
            return null;
        }
    }

    private static int inversions(int[] array){
        Integer inversions = 0;
        divide(array,inversions);

        return inversions;
    }

    private static void divide(int[] array,Integer inversions){
        int[] leftArray = split(array,Half.LEFT);
        int[] rightArray = split(array,Half.RIGHT);
        if(leftArray.length != 1)
            divide(leftArray,inversions);
        if(rightArray.length != 1)
            divide(rightArray,inversions);
        moveArray(merge(leftArray,rightArray,inversions),array);
    }

    private static int[] merge(int[] leftArray, int[] rightArray,Integer inversions) {
        int[] array = new int[leftArray.length+rightArray.length];

        int l=0, r=0;
        int multiplier=0, adder=0;
                                                        //TODO: LUCREZ PE ARRAY-URILE AUXILIARE:
        for(int i=0;i<array.length;){
            if(leftArray[l] <= 2*rightArray[r]) {       //adaug din stânga
                i++;
                l++;
                adder++;
            }
            else if(leftArray[l] > 2*rightArray[r]) {   //adaug din dreapta
                inv=inv+multiplier*adder;
                i++;
                r++;
                multiplier++;
                adder=0;
            }
            if(l==leftArray.length) {
                while (r < rightArray.length) {        //adaug numai din dreapta
                    i++;
                    r++;
                }
                inv=inv+multiplier*adder;
            }
            else if(r==rightArray.length) {
                while (l < leftArray.length) {         //adaug numai din stânga
                    i++;
                    l++;
                    adder++;
                }
                inv=inv+multiplier*adder;
            }
        }

        l=0;
        r=0;

        for(int i=0;i<array.length;){
            if(leftArray[l] <= rightArray[r]) {       //adaug din stânga
                array[i++] = leftArray[l++];
            }
            else if(leftArray[l] > rightArray[r]) {   //adaug din dreapta
                array[i++] = rightArray[r++];
            }
            if(l==leftArray.length) {
                while (r < rightArray.length) {        //adaug numai din dreapta
                    array[i++] = rightArray[r++];
                }
            }
            else if(r==rightArray.length) {
                while (l < leftArray.length) {         //adaug numai din stânga
                    array[i++] = leftArray[l++];
                }
            }
        }

        return array;
     }

    private static int[] split(int[] array, Half halfToReturn){
        int[] split;
        if(halfToReturn==Half.LEFT){
            split = new int[(int)(array.length/2)];
            for(int i=0;i<array.length/2;i++){
                split[i] = array[i];
            }
        }
        else {
            split = new int[array.length-array.length/2];
            for(int i=array.length/2;i<array.length;i++){
                split[i-array.length/2] = array[i];
            }
        }

        return split;
    }

    private static void moveArray(final int[] source, int[] destination){
        assert source.length == destination.length;

        for(int i=0;i<source.length;i++){
            destination[i] = source[i];
        }
    }

    @Deprecated
    public static void print(int[] array){
        for(int i=0;i<array.length;i++)
            System.out.print(array[i]+" ");
        System.out.println();
    }

}
