package drawer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The Drawer class is responsible for drawing the graph of a function
 * given by an expression into a coordinate system.
 * The coordinate system is then displayed on the BufferedImage in the Display class.
 */
public class Drawer {
    /**
     * Reference to the Graphics of the BufferedImage.
     */
    private static Graphics2D g;
    /**
     * Reference to the both the width and height of the image, as it
     * is a square.
     */
    private static int SIDE_LENGTH;
    /**
     * The amount of values displayed for each part of the axis.
     * This value is doubled for each axis, as there are SCALE_COUNT values
     * for both the positive and negative sides. There is also an additional value
     * for the origin.
     */
    private static final int SCALE_COUNT = 5;
    /**
     * Pixel offset for the position of each scale element on the screen.
     */
    private static final int SCALE_AXIS_OFFSET = 5;

    /**
     * Draws the graph corresponding to the expression onto img.
     * @param expression Mathematical function to be evaluated: x as in f(x) = x.
     * @param img Image to draw the Graph onto.
     * @param rangeX Specifies both the most negative and positive x Value.
     * @param rangeY Specifies both the most negative and positive y Value.
     * @param pos Offset applied to both ranges.
     */
    public static void drawGraph(String expression, BufferedImage img, double rangeX, double rangeY, Point pos) {
        g = (Graphics2D)img.getGraphics();
        SIDE_LENGTH = img.getWidth();
        ArrayList<Point> points = PixelCalculator.getPixelPoints(expression, rangeX, rangeY, pos, SIDE_LENGTH);
        g.setColor(Color.red);
        for (int i = 0; i < points.size()-1; i++)
            if (points.get(i+1).x - points.get(i).x == 1)
                g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        drawAxis(pos);
        drawScales(rangeX, rangeY, pos);
    }

    /**
     * Draws the scale values onto the screen.
     * @param rangeX Specifies both the most negative and positive x Value.
     * @param rangeY Specifies both the most negative and positive y Value.
     * @param pos Offset applied to both ranges.
     */
    private static void drawScales(double rangeX, double rangeY, Point pos) {
        g.setFont(g.getFont().deriveFont(12f));
        ArrayList<ScaleElement> elements = getScales(rangeX,rangeY,pos);
        elements.forEach(sE -> g.drawString(sE.value, sE.x, sE.y));
    }

    /**
     * Calculate the different values for each scale element.
     * @param rangeX Specifies both the most negative and positive x Value.
     * @param rangeY Specifies both the most negative and positive y Value.
     * @param pos Offset applied to both ranges.
     * @return Returns a List containing all scale elements with their
     * values corresponding to rangeX,rangeY and pos.
     */
    private static ArrayList<ScaleElement> getScales(double rangeX, double rangeY, Point pos) {
        ArrayList<ScaleElement> elements = new ArrayList<>();
        double xOffset = pos.x*(1.0/(SIDE_LENGTH/(2*rangeX)));
        double yOffset = pos.y*(1.0/(SIDE_LENGTH/(2*rangeY)));
        int xCord, yCord;
        for (int i = -SCALE_COUNT; i <= SCALE_COUNT; ++i) {
            double xScaleNumber = (i / (SCALE_COUNT+1.0)) * rangeX + xOffset;
            double yScaleNumber = (-i / (SCALE_COUNT+1.0)) * rangeY - yOffset;
            xCord = (int) ((SIDE_LENGTH / 2) * (1.0 + i / (SCALE_COUNT+1.0))) - (g.getFontMetrics().stringWidth(String.valueOf(i))+1);
            yCord = (int) ((SIDE_LENGTH / 2) * (1.0 + i / (SCALE_COUNT+1.0))) -3 + g.getFontMetrics().getHeight() / 2;
            elements.add(new ScaleElement(xScaleNumber,xCord,SIDE_LENGTH - SCALE_AXIS_OFFSET));
            elements.add(new ScaleElement(yScaleNumber, SCALE_AXIS_OFFSET , yCord));
        }
        return elements;
    }

    /**
     * Draws the coordinate axis onto the img.
     * @param pos Specifies the offset for the axis depending on scroll.
     */
    private static void drawAxis(Point pos) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.white);
        g.drawLine(0, (SIDE_LENGTH/2- pos.y),SIDE_LENGTH,(SIDE_LENGTH/2- pos.y)); //x
        g.drawLine((SIDE_LENGTH/2- pos.x),0,(SIDE_LENGTH/2- pos.x),SIDE_LENGTH); //y
        g.setStroke(new BasicStroke());
    }
}
