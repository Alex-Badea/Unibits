package polygon;

import java.awt.*;

/**
 * Created by balex on 29.12.2016.
 */
public class PolygonPoint extends Point {
    private final int initialIndex; //initialIndex reprez. efectiv numele vârfului în poligon

    ////
    PolygonPoint(int x, int y, int initialIndex) {
        super(x, y);
        this.initialIndex = initialIndex;
    }

    int getInitialIndex() {
        return initialIndex;
    }

    public int getSlantWith(PolygonPoint p2) {
        PolygonPoint p1 = this;

        return p1.getY() < p2.getY() ? 1 : -1;
    }

    protected int isInsideTriangle(PolygonPoint p1, PolygonPoint p2, PolygonPoint p3) {
        if(p1.orientation(p2, this) == Orientation.TANGENTIAL || p2.orientation(p3, this) == Orientation.TANGENTIAL || p3.orientation(p1, this) == Orientation.TANGENTIAL)
            return 0;
        if (p1.orientation(p2, this) == p2.orientation(p3, this) && p2.orientation(p3, this) == p3.orientation(p1, this))
            return 1;
        return -1;
    }

    Orientation orientation(PolygonPoint p2, PolygonPoint p3) { //O(1)
        PolygonPoint p1 = this;

        int val = (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y);

        if (val == 0) return Orientation.TANGENTIAL;  // coliniare
        return (val > 0) ? Orientation.CLOCKWISE : Orientation.COUNTERCLOCKWISE; // viraj dreapta : viraj stanga
    }


    @Override
    public String toString() {
        //return "("+x+","+y+")";
        return "" + initialIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != PolygonPoint.class)
            return false;

        PolygonPoint p1 = this;
        PolygonPoint p2 = (PolygonPoint) obj;
        return p1.x == p2.x && p1.y == p2.y;
    }
}
