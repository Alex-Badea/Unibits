import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by balex on 01.12.2016.
 */
public class Permutation {
    private enum Half{LEFT,RIGHT}

    private final int[] array;

    private int significantInversionsNo;
    ////
    public Permutation(String fileName) {
        int[] array;
        try {
            Scanner in = new Scanner((new File(fileName)));
            array = new int[in.nextInt()];
            for (int i = 0; in.hasNext(); i++) {
                array[i] = in.nextInt();
            }
        } catch (FileNotFoundException e) {System.out.println("Error"); array=null;}
        this.array = array;
    }

    public int getSignificantInversionsNo(){
        divide(array);

        return significantInversionsNo;
    }

    private void divide(int[] array){
        int[] leftArray = split(array,Half.LEFT);
        int[] rightArray = split(array,Half.RIGHT);

        if(leftArray.length != 1)
            divide(leftArray);

        if(rightArray.length != 1)
            divide(rightArray);

        copyArray(merge(leftArray,rightArray),array);
    }

    private int[] merge(int[] leftArray, int[] rightArray) {
        int[] array = new int[leftArray.length+rightArray.length];

        int l=0, r=0;
        int multiplier=0, adder=0;

        //TODO: SIMULEZ ARRAY AUXILIAR:
        for(int i=0;i<array.length;){
            if(leftArray[l] < 2*rightArray[r]) {       //adaug din stânga
                i++;
                l++;
                adder++;
            }
            else if(leftArray[l] >= 2*rightArray[r]) {   //adaug din dreapta
                significantInversionsNo=significantInversionsNo+multiplier*adder;
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
                significantInversionsNo=significantInversionsNo+multiplier*adder;
            }
            else if(r==rightArray.length) {
                while (l < leftArray.length) {         //adaug numai din stânga
                    i++;
                    l++;
                    adder++;
                }
                significantInversionsNo=significantInversionsNo+multiplier*adder;
            }
        }

        l=0;
        r=0;
        //TODO: ORDONEZ ARRAY PRINCIPAL:
        for(int i=0;i<array.length;){
            if(leftArray[l] < rightArray[r]) {       //adaug din stânga
                array[i++] = leftArray[l++];
            }
            else if(leftArray[l] >= rightArray[r]) {   //adaug din dreapta
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

    private int[] split(int[] array, Half halfToReturn){
        int[] split;

        if(halfToReturn==Half.LEFT){
            split = new int[array.length/2];
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

    private void copyArray(final int[] source, int[] destination){
        assert source.length == destination.length;

        for(int i=0;i<source.length;i++){
            destination[i] = source[i];
        }
    }
}
