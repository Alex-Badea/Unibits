import polygon.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by balex on 29.12.2016.
 */
public class Main {
    private static Point referencePoint;
    private static Polygon polygon;
    private static boolean firstClick = true;

    public static void main(String[] args) {
        JFrame coordSystem = new JFrame("Proiect GC");
        coordSystem.setResizable(false);
        coordSystem.setSize(500, 500);
        coordSystem.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ArrayList<Point> drawnPoints = new ArrayList<>();

        JPanel initializationPanel = new JPanel();
        JLabel instructions = new JLabel("Primul click determină poziția punctului, restul definesc vârfurile poligonului.");
        instructions.setBounds(10, 440, 500, 20);
        initializationPanel.add(instructions);

        JButton finalizeDrawingButton = new JButton("Finalizează poligon");
        finalizeDrawingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coordSystem.remove(initializationPanel);
                polygon = new Polygon(drawnPoints, referencePoint);
                coordSystem.add(polygon);
                coordSystem.setVisible(true);
            }
        });
        initializationPanel.add(finalizeDrawingButton);

        initializationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int x = e.getX() + 10;
                int y = e.getY() + 28;
                if (firstClick){
                    firstClick = false;
                    referencePoint = new Point(x, y);
                    coordSystem.getGraphics().drawOval((int)referencePoint.getX() - 2, (int)referencePoint.getY() - 2, 4, 4);
                }
                else {
                    coordSystem.getGraphics().fillOval(x - 2, y - 2, 4, 4);
                    System.out.println(x + "," + y);
                    drawnPoints.add(new Point(x, y));
                    Graphics graphics = coordSystem.getGraphics();
                    if (drawnPoints.size() >= 2) {
                        Point currentPoint = drawnPoints.get(drawnPoints.size() - 1);
                        Point prevPoint = drawnPoints.get(drawnPoints.size() - 2);
                        graphics.drawLine((int) currentPoint.getX(), (int) currentPoint.getY(), (int) prevPoint.getX(), (int) prevPoint.getY());
                    }
                }
            }
        });

        coordSystem.add(initializationPanel);
        coordSystem.setVisible(true);

        /*referencePoint = new Point(1,1);
        polygon = new Polygon("in.txt");
        coordSystem.add(polygon);

        coordSystem.setVisible(true);*/
    }
}
