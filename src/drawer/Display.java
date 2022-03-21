package drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Display extends JFrame {

    private static final int HEIGHT = 900;
    private static final int WIDTH = 1024;
    private static final int DISPLAY_SIZE = 512;
    private static final double DEFAULT_ZOOM = 20.0;
    private double zoom;
    private final JLabel display;
    private final JTextField parseField;
    private final BufferedImage image;
    private final Point relativePosition;
    private Point movedPos;
    private Point initialPosition;

    public Display() {
        image = new BufferedImage(DISPLAY_SIZE,DISPLAY_SIZE,BufferedImage.TYPE_3BYTE_BGR);
        zoom = DEFAULT_ZOOM;
        relativePosition = new Point(0,0);
        initialPosition = new Point(0, 0);
        movedPos = new Point(0,0);
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setFocusable(true);

        display = new JLabel();
        display.setBounds(256,0,DISPLAY_SIZE,DISPLAY_SIZE);
        display.setIcon(new ImageIcon(image));
        display.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                movedPos = e.getPoint();
                movedPos.x -= initialPosition.x;
                movedPos.y -= initialPosition.y;
                relativePosition.x -= movedPos.x;
                relativePosition.y -= movedPos.y;
                initialPosition = e.getPoint();
                updateScreen();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                initialPosition = e.getPoint();
            }
        });

        parseField = new JTextField();
        parseField.setBounds(375,HEIGHT-75,225,25);

        addMouseWheelListener(e -> {
            if (parseField.getText() != null) {
                zoom += e.getWheelRotation();
                if (zoom < 1 ||zoom > 10*DEFAULT_ZOOM) {
                    zoom -= e.getWheelRotation();
                    return;
                }
              updateScreen();
            }
        });

        JButton btnFinished = new JButton();
        btnFinished.setBounds(WIDTH-375,HEIGHT-75,50,25);
        btnFinished.addActionListener(e -> {
            zoom = DEFAULT_ZOOM;
            relativePosition.x = 0;
            relativePosition.y = 0;
            updateScreen();
        });

        getContentPane().add(parseField);
        getContentPane().add(display);
        getContentPane().add(btnFinished);
    }

    private synchronized void updateScreen() {
        image.getGraphics().clearRect(0,0,DISPLAY_SIZE,DISPLAY_SIZE);
        Drawer.drawGraph(parseField.getText(),image,zoom,zoom,relativePosition);
        display.update(display.getGraphics());
        requestFocus();
    }
}
