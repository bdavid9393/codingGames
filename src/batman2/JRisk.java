package batman2;

import java.awt.*;
import javax.swing.*;

public class JRisk {

    public static final int WIN_WIDTH = 700;
    public static final int WIN_HEIGHT = 700;

    private JFrame mainMap;

    private float scaleWidth;
    private float scaleHeight;

    private float height;
    private float width;
    private Point bomb;
    private Point batmanStart;

    private Polygon possibleBuilding;
    int round;

    public JRisk(float height, float width, Point bomb, Point batmanStart, Polygon pBuilding, int round) {
        this.scaleHeight = WIN_HEIGHT / height;
        this.scaleWidth = WIN_WIDTH / width;

        this.height = height * scaleHeight;
        this.width = width * scaleWidth;
        this.round = round;

        this.bomb = new Point(bomb.x * (int) scaleWidth, bomb.y * (int) scaleHeight);
        this.batmanStart = new Point(batmanStart.x / Player.ACCURACY * (int) scaleWidth, batmanStart.y / Player.ACCURACY * (int) scaleHeight);

        possibleBuilding = new Polygon();


        for (int i = 0; i != pBuilding.npoints; i++) {
            possibleBuilding.addPoint(pBuilding.xpoints[i] * (int) scaleWidth / Player.ACCURACY, pBuilding.ypoints[i] * (int) scaleHeight / Player.ACCURACY);
        }

        initComponents();
    }

    private void initComponents() {

        mainMap = new JFrame();
        mainMap.setResizable(false);

        mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);


                for (int i = 0; i <= width; i = (int) (i + scaleWidth)) {
                    g.drawLine(i, 0, i, (int) height);
                }
                for (int i = 0; i <= height; i = (int) (i + scaleHeight)) {
                    g.drawLine(0, i, (int) width, i);
                }

                g.setColor(Color.RED);
                drawXInCell(g, bomb);
                g.setColor(Color.BLACK);
                drawXInCell(g, batmanStart);


                Color myColour = new Color(0, 255, 0, 50);
                g.setColor(myColour);
                g.fillPolygon(possibleBuilding);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIN_WIDTH + 1, WIN_HEIGHT + 1);
            }
        };
        mainMap.add(p);
        mainMap.setTitle(String.valueOf(round));
        mainMap.pack();
        mainMap.setVisible(true);
    }

    private void drawXInCell(Graphics g, Point point) {
        g.drawLine(point.x, point.y, point.x + (int) scaleWidth, point.y + (int) scaleHeight);
        g.drawLine(point.x + (int) scaleWidth, point.y,
                point.x, point.y + (int) scaleHeight);
    }
}