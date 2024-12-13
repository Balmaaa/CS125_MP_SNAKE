import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

public class GameField extends JPanel implements KeyListener {

    public static final int PANEL_WIDTH = 600;
    public static final int PANEL_HEIGHT = 600;
    private List<Ellipse2D.Double> snakeParts1;
    private List<Ellipse2D.Double> snakeParts2;
    private Apple apple;
    private int snake1Direction; // 0: up, 1: down, 2: left, 3: right
    private int snake2Direction; // 0: up, 1: down, 2: left, 3: right
    private boolean running; // To control the game loop

    public GameField() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        initDefaults();
        setFocusable(true); // Make sure the panel is focusable
        addKeyListener(this); // Add the key listener
        running = true; // Set running to true
        new Thread(this::gameLoop).start(); // Start the game loop
    }

    public void initDefaults() {
        apple = new Apple(300, 300);
        snakeParts1 = Collections.synchronizedList(new ArrayList<>());
        snakeParts2 = Collections.synchronizedList(new ArrayList<>());

        // Initialize snake parts for player 1
        snakeParts1.add(new Ellipse2D.Double(400, 300, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 320, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 340, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 360, 20, 20));
        snake1Direction = 3; // Start moving to the right

        // Initialize snake parts for player 2
        snakeParts2.add(new Ellipse2D.Double(100, 100, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 120, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 140, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 160, 20, 20));
        snake2Direction = 3; // Start moving to the right
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the apple
        g2.setPaint(Color.RED);
        g2.fill(apple.getShape());

        // Draw player 1's snake parts
        g2.setPaint(new Color(34, 136, 215)); // BLUE
        for (Ellipse2D e : snakeParts1) {
            g2.fill(e);
        }

        // Draw player 2's snake parts
        g2.setPaint(new Color(255, 215, 0)); // YELLOW
        for (Ellipse2D e : snakeParts2) {
            g2.fill(e);
        }
    }

    // Game loop to update the snakes' positions
    private void gameLoop() {
        while (running) {
            moveSnake1();
            moveSnake2();
            repaint();
            try {
                Thread.sleep(100); // Control the speed of the game
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveSnake1() {
        // Get the head of the snake
        Ellipse2D.Double head = snakeParts1.get(0);
        double newX = head.getX();
        double newY = head.getY();

        // Update the position based on the current direction
        switch (snake1Direction) {
            case 0: // Up
                newY -= 20;
                break;
            case 1: // Down
                newY += 20;
                break;
            case 2: newX -= 20;
                break;
            case 3: // Right
                newX += 20;
                break;
        }

        // Add new head to the snake
        snakeParts1.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
        // Remove the last part of the snake
        if (snakeParts1.size() > 5) {
            snakeParts1.remove(snakeParts1.size() - 1);
        }
    }

    private void moveSnake2() {
        // Get the head of the snake
        Ellipse2D.Double head = snakeParts2.get(0);
        double newX = head.getX();
        double newY = head.getY();

        // Update the position based on the current direction
        switch (snake2Direction) {
            case 0: // Up
                newY -= 20;
                break;
            case 1: // Down
                newY += 20;
                break;
            case 2: // Left
                newX -= 20;
                break;
            case 3: // Right
                newX += 20;
                break;
        }

        // Add new head to the snake
        snakeParts2.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
        // Remove the last part of the snake
        if (snakeParts2.size() > 5) {
            snakeParts2.remove(snakeParts2.size() - 1);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Player 1 controls (WASD)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (snake1Direction != 1) snake1Direction = 0; // Up
                break;
            case KeyEvent.VK_S:
                if (snake1Direction != 0) snake1Direction = 1; // Down
                break;
            case KeyEvent.VK_A:
                if (snake1Direction != 3) snake1Direction = 2; // Left
                break;
            case KeyEvent.VK_D:
                if (snake1Direction != 2) snake1Direction = 3; // Right
                break;
        }

        // Player 2 controls (Arrow keys)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (snake2Direction != 1) snake2Direction = 0; // Up
                break;
            case KeyEvent.VK_DOWN:
                if (snake2Direction != 0) snake2Direction = 1; // Down
                break;
            case KeyEvent.VK_LEFT:
                if (snake2Direction != 3) snake2Direction = 2; // Left
                break;
            case KeyEvent.VK_RIGHT:
                if (snake2Direction != 2) snake2Direction = 3; // Right
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}