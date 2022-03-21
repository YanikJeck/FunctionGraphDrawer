package drawer;

import java.awt.*;
import java.util.ArrayList;

public class PixelCalculator {

    /**
     * Reference to the both the width and height of the image, as it
     * is a square.
     */
    private static int SIDE_LENGTH;

    /**
     * Retrieves a list of points representing a pixel on the BufferedImage
     * which are part of the Graph and should therefore be colored accordingly.
     * The function maps pixels to a point in the coordinate system.
     * This is achieved by taking SIDE_LENGTH pixels for the x value,
     * transforming them into an x coordinate based on rangeX and pos and
     * finally evaluating this x value in terms of the expression. The resulting
     * x and y value are then remapped onto the pixel range specified by SIDE_LENGTH.
     * @param expression Mathematical function used to get the y values for each x.
     * @param rangeX Specifies both the most negative and positive x Value.
     * @param rangeY Specifies both the most negative and positive y Value.
     * @param pos Offset applied to both ranges.
     * @param length Used to set SIDE_LENGTH.
     * @return Returns an ArrayList containing SIDE_LENGTH points such that
     * each point represents a pixel coordinate on the image.
     */
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

    /**
     * Maps the x and y values to its corresponding pixel coordinate.
     * @param x X value to be mapped.
     * @param y Y value to be mapped.
     * @param rangeX Specifies both the most negative and positive x Value.
     * @param rangeY Specifies both the most negative and positive y Value.
     * @param pos Offset applied to both ranges.
     * @param points ArrayList to store the new point.
     */
    private static void addPoint(double x, double y, double rangeX, double rangeY,Point pos, ArrayList<Point> points) {
        if (Double.isNaN(y))
            return;
        int pixelX = (int)((SIDE_LENGTH/2) *(1.0 + x/rangeX))-pos.x;
        int pixelY = (int)((SIDE_LENGTH/2) *(1.0 - y/rangeY))-pos.y;
        points.add(new Point(pixelX,pixelY));
    }
}
