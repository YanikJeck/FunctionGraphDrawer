package drawer;

import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static org.junit.jupiter.api.Assertions.*;

class DrawerTest {

    @Test
    void drawGraph() {
        int[][] imgRGBs = new int[2][4*512*512];
        BufferedImage img = new BufferedImage(512,512,TYPE_3BYTE_BGR);
        BufferedImage imgCopy = new BufferedImage(512,512,TYPE_3BYTE_BGR);
        Graphics2D gCopy = (Graphics2D) imgCopy.getGraphics();
        gCopy.setColor(Color.red);
        gCopy.drawLine(0,512,512,0);
        gCopy.setColor(Color.white);
        gCopy.setStroke(new BasicStroke(2));
        gCopy.drawLine(0, 256,512,256); //x
        gCopy.drawLine(256,0,256,512); //y
        gCopy.setStroke(new BasicStroke(1));
        drawScales(gCopy);
        Drawer.drawGraph("x", img,20,20, new Point(0,0));
        img.getRGB(0,0,512,512,imgRGBs[0],0,0);
        imgCopy.getRGB(0,0,512,512,imgRGBs[1],0,0);
        assertEquals(Arrays.toString(imgRGBs[0]),Arrays.toString(imgRGBs[1]));
    }

    private void drawScales(Graphics2D g) {
        g.setFont(g.getFont().deriveFont(12f));
        ArrayList<ScaleElement> elements = getScales(g);
        elements.forEach(sE -> g.drawString(sE.value, sE.x, sE.y));
    }

    private ArrayList<ScaleElement> getScales(Graphics2D g) {
        ArrayList<ScaleElement> elements = new ArrayList<>();
        int xCord, yCord;
        for (int i = -5; i <= 5; ++i) {
            double xScaleNumber = (i / (5+1.0));
            double yScaleNumber = (-i / (5+1.0));
            xCord = (int) ((5 / 2) * (1.0 + i / (6.0))) - (g.getFontMetrics().stringWidth(String.valueOf(i))+1);
            yCord = (int) ((5 / 2) * (1.0 + i / (6.0))) -3 + g.getFontMetrics().getHeight() / 2;
            elements.add(new ScaleElement(xScaleNumber,xCord,507));
            elements.add(new ScaleElement(yScaleNumber, 5 , yCord));
        }
        return elements;
    }

}