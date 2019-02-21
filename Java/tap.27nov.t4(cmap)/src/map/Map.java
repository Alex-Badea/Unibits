package map;

import com.sun.istack.internal.Nullable;
import sun.plugin.javascript.navig.Array;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by balex on 22.11.2016.
 */
public class Map {
    private Coordinate[] coordsSortedByX;
    private Coordinate[] coordsSortedByY;
    private int coordinatesNo;
    ////
    public Map(String fileName){
        try{
            Coordinate[] coordinates;
            Scanner in = new Scanner(new File(fileName));
            coordinatesNo = in.nextInt();
            coordinates = new Coordinate[coordinatesNo];
            for(int i=0;i<coordinatesNo;i++){
                coordinates[i] = new Coordinate(in.nextInt(),in.nextInt());
            }
            coordsSortedByX = sortByX(coordinates);
            coordsSortedByY = sortByY(coordinates);
        }catch (FileNotFoundException e){System.out.println("Error");}
    }

    private Coordinate[] sortByX(Coordinate[] coordinates){
        Coordinate[] aux = new Coordinate[coordinates.length];

        Arrays.sort(coordinates, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate c1, Coordinate c2) {
                if(c1.getX() == c2.getX())
                    return 0;
                return c1.getX() < c2.getX() ? -1 : 1;
            }
        });

        System.arraycopy(coordinates,0,aux,0,coordinates.length);

        return aux;
    }

    private Coordinate[] sortByY(Coordinate[] coordinates){
        Coordinate[] aux = new Coordinate[coordinates.length];

        Arrays.sort(coordinates, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate c1, Coordinate c2) {
                if(c1.getY() == c2.getY())
                    return 0;
                return c1.getY() < c2.getY() ? -1 : 1;
            }
        });

        System.arraycopy(coordinates,0,aux,0,coordinates.length);

        return aux;
    }

    public double cmap(){
        return leastDistanceWithin(coordsSortedByY,0,coordinatesNo-1);
    }

    private double leastDistanceWithin(Coordinate[] coordsWithinMap, int leftmostIndex, int rightmostIndex){  //O(nlogn)
        if(leftmostIndex==rightmostIndex-1)
            return Coordinate.distance(coordsSortedByX[leftmostIndex],coordsSortedByX[rightmostIndex]);
        else {
            int medianIndex = (leftmostIndex+rightmostIndex)/2;

            Coordinate[] coordsInLeftSubmap = filterCoordsWithinBounds(coordsWithinMap,coordsSortedByX[leftmostIndex].getX(),coordsSortedByX[medianIndex].getX());
            double d1 = leastDistanceWithin(coordsInLeftSubmap,leftmostIndex,medianIndex); //O(nlogn)
            Coordinate[] coordsInRightSubmap = filterCoordsWithinBounds(coordsWithinMap,coordsSortedByX[medianIndex].getX(),coordsSortedByX[rightmostIndex].getX());
            double d2 = leastDistanceWithin(coordsInRightSubmap,medianIndex,rightmostIndex); //O(nlogn)

            double δ = Math.min(d1, d2);
            return Math.min(δ, leastDistanceAcross(coordsWithinMap,δ,medianIndex));//O(n)
        }
    }

    private double leastDistanceAcross(Coordinate[] coordsWithinMap, double borderSize, int medianIndex){       //O(n)
        double leastDistanceAcross = Double.MAX_VALUE;
        Coordinate[] coordsWithinBorder = filterCoordsWithinBounds(coordsWithinMap,coordsSortedByX[medianIndex].getX()-borderSize,coordsSortedByX[medianIndex].getX()+borderSize);     //O(n)

        if(coordsWithinBorder != null)
            for(int i=0;i<coordsWithinBorder.length-1;i++){                                     //O(n)
                for(int j=i+1;j<=Math.min(i+7,coordsWithinBorder.length-1);j++){
                    if(Coordinate.distance(coordsWithinBorder[i],coordsWithinBorder[j])<leastDistanceAcross)
                        leastDistanceAcross = Coordinate.distance(coordsWithinBorder[i],coordsWithinBorder[j]);
                }
            }

        return leastDistanceAcross;
    }

    private Coordinate[] filterCoordsWithinBounds(Coordinate[] coordinates, double leftBound, double rightBound){      //O(n)
        ArrayList<Coordinate> coordsWithinBounds = new ArrayList<>();

        for(Coordinate c : coordinates){   //O(n)
            if(c.hasXBetween(leftBound,rightBound))
                coordsWithinBounds.add(c);
        }

        Coordinate[] aux = new Coordinate[coordsWithinBounds.size()];
        for(int i = 0; i<aux.length; i++){
            aux[i] = coordsWithinBounds.get(i);
        }

        return coordsWithinBounds.isEmpty()? null : aux;
    }
}
