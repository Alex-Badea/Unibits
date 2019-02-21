package map;

/**
 * Created by balex on 22.11.2016.
 */
public class Coordinate {
    private int x, y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public static double distance(Coordinate c1, Coordinate c2){
        return Math.sqrt(Math.pow((c1.x-c2.x),2)+(Math.pow((c1.y-c2.y),2)));
    }
    public boolean hasXBetween(double leftBound, double rightBound){
        return (this.x>=leftBound && this.x<=rightBound);
    }
    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
