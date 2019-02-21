package polygon;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * Created by balex on 29.12.2016.
 */
public class Polygon extends JPanel {
    private final ArrayList<PolygonPoint> points;
    private final ArrayList<Polygon> triangulation;
    private final Orientation polygonOrientation;
    private PolygonPoint referencePoint;
    private Map.Entry<Polygon, Integer> triangleContainingRefPoint;

    ////
    public Polygon(ArrayList<Point> points, Point referencePoint) { //O(n^2)
        this.referencePoint = new PolygonPoint((int) referencePoint.getX(), (int) referencePoint.getY(), -1);
        this.points = new ArrayList<>();
        for (int i = 0; i < points.size(); i++)
            this.points.add(new PolygonPoint((int) points.get(i).getX(), (int) points.get(i).getY(), i));

        polygonOrientation = getPolygonOrientation();

        triangulation = getTriangulation();

        triangleContainingRefPoint = getTriangleContainingRefPoint();
    }

    private Polygon(PolygonPoint p1, PolygonPoint p2, PolygonPoint p3) { //generează triunghi
        points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);

        triangulation = null;
        polygonOrientation = null;
    }

    private Map.Entry<Polygon, Integer> getTriangleContainingRefPoint() {
        for (Polygon p : triangulation) {
            if (referencePoint.isInsideTriangle(p.points.get(0), p.points.get(1), p.points.get(2)) == 1)
                return new AbstractMap.SimpleEntry<>(p, 1);
            else if (referencePoint.isInsideTriangle(p.points.get(0), p.points.get(1), p.points.get(2)) == 0)
                return new AbstractMap.SimpleEntry<>(p, 0);
        }

        return null;
    }

    private ArrayList<Polygon> getTriangulation() {
        ArrayList<Polygon> triangleList = new ArrayList<>();

        ArrayList<PolygonPoint> currentContext = new ArrayList<>(); //contextul curent reprezintă vârfurile încă neeliminate din poligon
        for (int i = 0; i < points.size(); i++)
            currentContext.add(points.get(i));
        ArrayList<PolygonPoint> reflexPoints = initializeReflexPoints();
        ArrayList<PolygonPoint> convexPoints = initializeConvexPoints();
        ArrayDeque<PolygonPoint> earTips = initializeEarTips(convexPoints, reflexPoints);//TODO O(n^2)

        while (!earTips.isEmpty()) { //fiecare ear este procesat o singură dată după care eliminat din earTips //TODO O(n) * O(n)
            if (currentContext.size() == 3) {   //triangularea trivială
                triangleList.add(new Polygon(currentContext.get(0), currentContext.get(1), currentContext.get(2)));
                break;
            } else {
                PolygonPoint currentPoint = earTips.removeFirst();
                PolygonPoint adjPoint1 = getAdjacentPoints(currentContext, currentPoint)[0];//TODO O(n)
                PolygonPoint adjPoint2 = getAdjacentPoints(currentContext, currentPoint)[1];//TODO O(n)

                boolean adjPoint1WasReflex = isReflex(currentContext, adjPoint1);
                boolean adjPoint2WasReflex = isReflex(currentContext, adjPoint2);
                boolean adjPoint1WasEarTip = isEarTip(currentContext, reflexPoints, adjPoint1);
                boolean adjPoint2WasEarTip = isEarTip(currentContext, reflexPoints, adjPoint2);

                triangleList.add(new Polygon(adjPoint1, currentPoint, adjPoint2));
                currentContext.remove(currentPoint);                                //TODO SE SCHIMBĂ CONTEXTUL

                boolean adjPoint1IsReflex = isReflex(currentContext, adjPoint1);
                boolean adjPoint2IsReflex = isReflex(currentContext, adjPoint2);
                boolean adjPoint1isEarTip = isEarTip(currentContext, reflexPoints, adjPoint1);//TODO O(n)
                boolean adjPoint2isEarTip = isEarTip(currentContext, reflexPoints, adjPoint2);//TODO O(n)

                //"era" și "este" în raport cu contextul actual

                //dacă "era" reflex și nu mai "este" reflex se înlătură din reflexPoints și se adaugă la convexPoints
                if (adjPoint1WasReflex && !adjPoint1IsReflex) {
                    reflexPoints.remove(adjPoint1);//TODO O(n)
                    convexPoints.add(adjPoint1);
                }
                if (adjPoint2WasReflex && !adjPoint2IsReflex) {
                    reflexPoints.remove(adjPoint2);//TODO O(n)
                    convexPoints.add(adjPoint2);
                }

                //dacă nu "era" reflex și nu "era" ear tip, dacă este ear tip în contextul curent, se adaugă la earTips
                if (!adjPoint1IsReflex && !adjPoint1WasEarTip)
                    if (isEarTip(currentContext, reflexPoints, adjPoint1))
                        earTips.addFirst(adjPoint1);
                if (!adjPoint2IsReflex && !adjPoint2WasEarTip)
                    if (isEarTip(currentContext, reflexPoints, adjPoint2))
                        earTips.addFirst(adjPoint2);

                //dacă "era" ear tip și nu mai "este" ear tip se înlătură din earTips
                if (adjPoint1WasEarTip && !adjPoint1isEarTip)
                    earTips.remove(adjPoint1);//TODO O(n)
                if (adjPoint2WasEarTip && !adjPoint2isEarTip)
                    earTips.remove(adjPoint2);//TODO O(n)
            }
        }

        return triangleList;
    }

    private boolean isEarTip(ArrayList<PolygonPoint> context, ArrayList<PolygonPoint> reflexPoints, PolygonPoint point) {
        PolygonPoint prevPoint = getAdjacentPoints(context, point)[0];
        PolygonPoint nextPoint = getAdjacentPoints(context, point)[1];

        if (isReflex(context, point))
            return false;

        for (int i = 0; i < reflexPoints.size(); i++)
            if (reflexPoints.get(i).isInsideTriangle(prevPoint, point, nextPoint) == 1)
                return false;

        return true;
    }

    private boolean isReflex(ArrayList<PolygonPoint> context, PolygonPoint point) {
        PolygonPoint prevPoint = getAdjacentPoints(context, point)[0];
        PolygonPoint nextPoint = getAdjacentPoints(context, point)[1];

        if (prevPoint.orientation(point, nextPoint) != polygonOrientation)
            return true;
        return false;
    }

    private PolygonPoint[] getAdjacentPoints(ArrayList<PolygonPoint> context, PolygonPoint polygonPoint) { //TODO NU MAI MULT DE O(n) (eventual O(1))!!!!!!!
        PolygonPoint prevPoint = context.get(context.size() - 1);
        PolygonPoint nextPoint = context.get(1);
        for (int i = 0; i < context.size(); i++) {
            if (context.get(i).equals(polygonPoint))
                break;

            prevPoint = context.get(i);
            nextPoint = context.get(Math.floorMod(i + 2, context.size()));
        }

        return new PolygonPoint[]{prevPoint, nextPoint};
    }

    private ArrayList<PolygonPoint> initializeReflexPoints() {
        ArrayList<PolygonPoint> points = new ArrayList<>();

        for (int i = 0; i < this.points.size(); i++) {
            PolygonPoint currentPoint = this.points.get(i);
            PolygonPoint prevPoint = getAdjacentPoints(this.points, currentPoint)[0];
            PolygonPoint nextPoint = getAdjacentPoints(this.points, currentPoint)[1];

            if (prevPoint.orientation(currentPoint, nextPoint) != polygonOrientation)
                points.add(currentPoint);
        }

        return points;
    }

    private ArrayList<PolygonPoint> initializeConvexPoints() {
        ArrayList<PolygonPoint> points = new ArrayList<>();

        for (int i = 0; i < this.points.size(); i++) {
            PolygonPoint currentPoint = this.points.get(i);
            PolygonPoint prevPoint = getAdjacentPoints(this.points, currentPoint)[0];
            PolygonPoint nextPoint = getAdjacentPoints(this.points, currentPoint)[1];

            if (prevPoint.orientation(currentPoint, nextPoint) == polygonOrientation)
                points.add(currentPoint);
        }

        return points;
    }

    private ArrayDeque<PolygonPoint> initializeEarTips(ArrayList<PolygonPoint> convexPoints, ArrayList<PolygonPoint> reflexPoints) {
        ArrayDeque<PolygonPoint> earTips = new ArrayDeque<>();

        for (int i = 0; i < convexPoints.size(); i++)
            if (isEarTip(this.points, reflexPoints, convexPoints.get(i)))
                earTips.add(convexPoints.get(i));

        return earTips;
    }

    private Orientation getPolygonOrientation() { //O(n^2)
        PolygonPoint maxYPoint = points.get(0);

        for (int i = 0; i < points.size(); i++) {
            PolygonPoint currentPoint = points.get(i);

            if (currentPoint.getY() > maxYPoint.getY())
                maxYPoint = currentPoint;
        }
        PolygonPoint p1 = getAdjacentPoints(this.points, maxYPoint)[0];
        PolygonPoint p2 = maxYPoint;
        PolygonPoint p3 = getAdjacentPoints(this.points, maxYPoint)[1];

        return p1.orientation(p2, p3);
    }

    @Override
    public void paintComponent(Graphics graphics) { //O(n^2)
        super.paintComponent(graphics);

        if (referencePoint != null) {
            graphics.drawOval((int) referencePoint.getX() - 3, (int) referencePoint.getY() - 3, 4, 4);
            JLabel label = new JLabel("A");
            label.setBounds((int) referencePoint.getX(), (int) referencePoint.getY(), 20, 20);
            add(label);
        }

        for (int i = 0; i < points.size(); i++) {
            int currentX = (int) points.get(i).getX();
            int currentY = (int) points.get(i).getY();
            int nextX = (int) points.get((i + 1) % points.size()).getX();
            int nextY = (int) points.get((i + 1) % points.size()).getY();

            graphics.fillOval(currentX - 3, currentY - 3, 6, 6);
            graphics.drawLine(currentX, currentY, nextX, nextY);
            JLabel label = new JLabel("" + i);
            label.setBounds(currentX, currentY, 20, 20);
            add(label);
        }

        for (int i = 0; i < triangulation.size(); i++) {
            PolygonPoint p1 = triangulation.get(i).points.get(0);
            PolygonPoint p2 = triangulation.get(i).points.get(1);
            PolygonPoint p3 = triangulation.get(i).points.get(2);

            int x1 = (int) p1.getX();
            int x2 = (int) p2.getX();
            int x3 = (int) p3.getX();

            int y1 = (int) p1.getY();
            int y2 = (int) p2.getY();
            int y3 = (int) p3.getY();

            graphics.drawLine(x1, y1, x2, y2);
            graphics.drawLine(x2, y2, x3, y3);
            graphics.drawLine(x3, y3, x1, y1);
        }

        String text;
        if (triangleContainingRefPoint != null) {
            if (triangleContainingRefPoint.getValue() == 0)
                text = "Punctul A se află pe marginea " + triangleContainingRefPoint.getKey();
            else if (triangleContainingRefPoint.getValue() == 1)
                text = "Punctul A se află în " + triangleContainingRefPoint.getKey();
            else text = "EROARE";
        } else
            text = "Punctul A se află în exteriorul poligonului";

        JLabel label = new JLabel(text);
        label.setBounds(10, 440, 500, 20);
        add(label);
    }

    @Override
    public String toString() {
        return points.toString();
    }

    @Deprecated
    public void printArray(Object[] array) {
        for (Object o : array) {
            System.out.print(o + " ");
        }
        System.out.println();
    }

    @Deprecated
    public Polygon(String fileName) {
        ArrayList<PolygonPoint> points;
        try {
            Scanner in = new Scanner(new File(fileName));
            points = new ArrayList<>();
            for (int i = 0; in.hasNext(); i++)
                points.add(new PolygonPoint(in.nextInt(), in.nextInt(), i));
        } catch (Exception e) {
            points = null;
            System.out.println("Error.");
        }

        this.points = points;

        polygonOrientation = getPolygonOrientation();

        triangulation = getTriangulation();
    }
}
