package table;

/**
 * Created by balex on 22.11.2016.
 */
public class Coordinate {
    private int x, y;
    ////
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
}
