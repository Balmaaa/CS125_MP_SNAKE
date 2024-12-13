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
import java.util.Random;
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
    private int score1; // Score for player 1
    private int score2; // Score for player 2

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

        score1 = 0; // Initialize score for player 1
        score2 = 0; // Initialize score for player 2
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

        // Draw scores
        g2.setPaint(Color.WHITE);
        g2.drawString("Player 1 Score: " + score1, 10, 20);
        g2.drawString("Player 2 Score: " + score2, 10, 40);
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
            case 2: // Left
                newX -= 20;
                break;
            case 3: // Right
                newX += 20;
                break;
        }

        // Check for wall collisions
        if (newX < 0 || newX >= PANEL_WIDTH || newY < 0 || newY >= PANEL_HEIGHT) {
            handleGameOver(true); // Player 1 loses
            return;
        }

        // Check for self-collision
        for (int i = 1; i < snakeParts1.size(); i++) {
            if (snakeParts1.get(i).getX() == newX && snakeParts1.get(i).getY() == newY) {
                handleGameOver(true); // Player 1 loses
                return;
            }
        }

        // Check for apple collision
        if (apple.getShape().intersects(newX, newY, 20, 20)) {
            score1++; // Increase score for player 1
            repositionApple(); // Reposition the apple
        } else {
            // Remove the last part of the snake if not eating
            if (snakeParts1.size() > 5) {
                snakeParts1.remove(snakeParts1.size() - 1);
            }
        }

        // Add new head to the snake
        snakeParts1.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
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

        // Check for wall collisions
        if (newX < 0 || newX >= PANEL_WIDTH || newY < 0 || newY >= PANEL_HEIGHT) {
            handleGameOver(false); // Player 2 loses
            return;
        }

        // Check for self-collision
        for (int i = 1; i < snakeParts2.size(); i++) {
            if (snakeParts2.get(i).getX() == newX && snakeParts2.get(i).getY() == newY) {
                handleGameOver(false); // Player 2 loses
                return;
            }
        }

        // Check for apple collision
        if (apple.getShape().intersects(newX, newY, 20, 20)) {
            score2++; // Increase score for player 2
            repositionApple(); // Reposition the apple
        } else {
            // Remove the last part of the snake if not eating
            if (snakeParts2.size() > 5) {
                snakeParts2.remove(snakeParts2.size() - 1);
            }
        }

        // Add new head to the snake
        snakeParts2.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
    }

    private void repositionApple() {
        Random rand = new Random();
        int newX, newY;
        boolean validPosition;

        do {
            newX = rand.nextInt(PANEL_WIDTH / 20) * 20;
            newY = rand.nextInt(PANEL_HEIGHT / 20) * 20;
            validPosition = true;

            // Check if the new apple position overlaps with the snake parts
            for (Ellipse2D.Double part : snakeParts1) {
                if (part.getX() == newX && part.getY() == newY) {
                    validPosition = false;
                    break;
                }
            }
            for (Ellipse2D.Double part : snakeParts2) {
                if (part.getX() == newX && part.getY() == newY) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);

        apple.setPosition(newX, newY); // Update the apple's position
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

    public void handleGameOver(boolean isPlayer1) {
        // Logic to handle game over state
        System.out.println((isPlayer1 ? "Player 1" : "Player 2") + " has lost!");
        running = false; // Stop the game loop
        // Additional game-over handling can be added here, such as displaying a message
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