import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

public class GameField extends JPanel {

    public static final int PANEL_WIDTH = 400;
    public static final int PANEL_HEIGHT = 400;

    private List<Ellipse2D.Double> snakeParts1;
    private List<Ellipse2D.Double> snakeParts2;
    private Apple apple;

    public GameField() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        initDefaults();
    }

    public void initDefaults() {
        apple = new Apple(100, 100); // Initialize the apple
        snakeParts1 = Collections.synchronizedList(new ArrayList<>());
        snakeParts2 = Collections.synchronizedList(new ArrayList<>());

        // Initialize snake parts for player 1
        snakeParts1.add(new Ellipse2D.Double(260, 260, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(260, 280, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(260, 300, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(260, 320, 20, 20));

        // Initialize snake parts for player 2
        snakeParts2.add(new Ellipse2D.Double(100, 100, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 120, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 140, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 160, 20, 20));
    }

    public void setSnakeParts1(List<Ellipse2D.Double> snakeParts) {
        this.snakeParts1 = snakeParts;
    }

    public void setSnakeParts2(List<Ellipse2D.Double> snakeParts) {
        this.snakeParts2 = snakeParts;
    }

    public Apple getApple() {
        return apple;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the apple
        g2.setPaint(Color.WHITE);
        g2.fillOval((int) apple.getShape().getX(), (int) apple.getShape().getY(), Apple.SIZE, Apple.SIZE);

        // Draw player 1's snake parts
        g2.setPaint(new Color(34, 136, 215)); // BLUE
        for (Ellipse2D e : snakeParts1) {
            g2.fill(e);
        }

        // Draw player 1's snake head
        g2.setPaint(new Color(215, 34, 38));  // RED
        g2.fill(snakeParts1.get(0));

        // Draw player 2's snake parts
        g2.setPaint(new Color(255, 215, 0)); // YELLOW
        for (Ellipse2D e : snakeParts2) {
            g2.fill(e);
        }

        // Draw player 2's snake head
        g2.setPaint(new Color(0, 255, 0)); // GREEN
        g2.fill(snakeParts2.get(0));
    }
}
