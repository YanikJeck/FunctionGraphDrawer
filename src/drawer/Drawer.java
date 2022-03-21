package drawer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Drawer {
    private static Graphics2D g;
    private static int SIDE_LENGTH;
    private static final int SCALE_COUNT = 5;
    private static final int SCALE_AXIS_OFFSET = 5;

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

    private static void drawScales(double rangeX, double rangeY, Point pos) {
        g.setFont(g.getFont().deriveFont(12f));
        ArrayList<ScaleElement> elements = getScales(rangeX,rangeY,pos);
        elements.forEach(sE -> g.drawString(sE.value, sE.x, sE.y));
    }

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

    private static void drawAxis(Point pos) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.white);
        g.drawLine(0, (SIDE_LENGTH/2- pos.y),SIDE_LENGTH,(SIDE_LENGTH/2- pos.y)); //x
        g.drawLine((SIDE_LENGTH/2- pos.x),0,(SIDE_LENGTH/2- pos.x),SIDE_LENGTH); //y
        g.setStroke(new BasicStroke());
    }
}
