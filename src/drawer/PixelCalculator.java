package drawer;

import java.awt.*;
import java.util.ArrayList;

public class PixelCalculator {

    private static int SIDE_LENGTH;

    public static ArrayList<Point> getPixelPoints(String expression, double rangeX, double rangeY, Point pos, int length) {
        SIDE_LENGTH = length;
        ArrayList<Point> points = new ArrayList<>();
        double x,y;
        int startPosition = -(SIDE_LENGTH/2)+pos.x;
        int endPosition = startPosition+SIDE_LENGTH;
        for (int i = startPosition; i < endPosition; i++) {
            x = ((i)*(1.0/(SIDE_LENGTH/(2*rangeX))));
            y = Parser.eval(expression,x);
            addPoint(x, y, rangeX, rangeY, pos, points);
        }
        return points;
    }

    private static void addPoint(double x, double y, double rangeX, double rangeY,Point pos, ArrayList<Point> points) {
        if (Double.isNaN(y))
            return;
        int pixelX = (int)((SIDE_LENGTH/2) *(1.0 + x/rangeX))-pos.x;
        int pixelY = (int)((SIDE_LENGTH/2) *(1.0 - y/rangeY))-pos.y;
        points.add(new Point(pixelX,pixelY));
    }
}
