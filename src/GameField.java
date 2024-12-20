import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameField extends JPanel implements KeyListener {

    public static final int PANEL_WIDTH = 600;
    public static final int PANEL_HEIGHT = 600;
    private Apple apple;
    private int snake1Direction;
    private int snake2Direction;
    private boolean running;
    private int score1;
    private int score2;
    private ScorePanel scorePanel; // Declare scorePanel here
    private SnakeFrame snakeFrame; // Reference to the SnakeFrame
    private Snake player1;
    private Snake player2;

    public GameField(SnakeFrame snakeFrame) {
        this.snakeFrame = snakeFrame; // Store the reference
        this.scorePanel = new ScorePanel(); // Initialize ScorePanel
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        initDefaults();
        setFocusable(true);
        addKeyListener(this);
        running = true;
        new Thread(this::gameLoop).start();
    }

    public synchronized void initDefaults() {
        apple = new Apple(300, 300);
        player1 = new Snake(this, scorePanel, Direction.RIGHT, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(this, scorePanel, Direction.LEFT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, false);

        // Initialize snake directions
        snake1Direction = 3; // Right
        snake2Direction = 3; // Right

        // Player Scoreboard
        score1 = 0;
        score2 = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the grid with a subtle color
        g2.setColor(new Color(240, 240, 240, 50));
        for (int i = 0; i < PANEL_WIDTH; i += 15) {
            g2.drawLine(i, 0, i, PANEL_HEIGHT);
        }
        for (int i = 0; i < PANEL_HEIGHT; i += 15) {
            g2.drawLine(0, i, PANEL_WIDTH, i);
        }

        // Draw a solid background for the scoreboard area (top 50px)
        g2.setColor(new Color(50, 50, 50));  // Dark gray for the scoreboard
        g2.fillRect(0, 0, PANEL_WIDTH, 60);  // This covers the top 50px of the panel

        // Draw Apple
        g2.setPaint(Color.RED);
        g2.fill(apple.getShape());

        // Draw Snake 1
        g2.setPaint(new Color(34, 136, 215));
        for (Ellipse2D e : player1.getParts()) {
            g2.fill(e);
        }

        // Draw Snake 2
        g2.setPaint(new Color(255, 215, 0));
        for (Ellipse2D e : player2.getParts()) {
            g2.fill(e);
        }

        // Draw Scoreboard
        g2.setPaint(Color.WHITE);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g2.drawString("Player 1: " + score1, 30, 40);  // Position it inside the solid scoreboard area
        g2.drawString("Player 2: " + score2, PANEL_WIDTH - 180, 40);  // Position it inside the solid scoreboard area
    }

    private void gameLoop() {
        while (running) {
            player1.moveSnake(); // Move player 1's snake
            player2.moveSnake(); // Move player 2's snake
            checkCollisions(); // Check for collisions
            repaint();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCollisions() {
        // Check for boundary collisions and self-collisions for player 1
        Ellipse2D.Double head1 = player1.getParts().get(0);
        double newX1 = head1.getX();
        double newY1 = head1.getY();

        // Check for boundary collision (wall collision)
        if (newX1 < 0 || newX1 >= PANEL_WIDTH || newY1 < 50 || newY1 >= PANEL_HEIGHT) {
            handleGameOver(true);  // Player 1 lost
            return;
        }

        // Check if snake 1 collides with itself
        for (int i = 1; i < player1.getParts().size(); i++) {
            if (player1.getParts().get(i).getX() == newX1 && player1.getParts().get(i).getY() == newY1) {
                handleGameOver(true);  // Player 1 lost
                return;
            }
        }

        // Check for snake 1 colliding with snake 2
        for (Ellipse2D.Double part : player2.getParts()) {
            if (part.getX() == newX1 && part.getY() == newY1) {
                handleGameOver(true);  // Player 1 lost
                return;
            }
        }

        // Check for apple collision for player 1
        if (apple.getShape().intersects(newX1, newY1, 15, 15)) {
            score1++;
            repositionApple();
        } else {
            if (player1.getParts().size() > 5) {
                player1.getParts().remove(player1.getParts().size() - 1);
            }
        }

        // Check for boundary collisions and self-collisions for player 2
        Ellipse2D.Double head2 = player2.getParts().get(0);
        double newX2 = head2.getX();
        double newY2 = head2.getY();

        // Check for boundary collision (wall collision)
        if (newX2 < 0 || newX2 >= PANEL_WIDTH || newY2 < 50 || newY2 >= PANEL_HEIGHT) {
            handleGameOver(false);  // Player 2 lost
            return;
        }

        // Check if snake 2 collides with itself
        for (int i = 1; i < player2.getParts().size(); i++) {
            if (player2.getParts().get(i).getX() == newX2 && player2.getParts().get(i).getY() == newY2) {
                handleGameOver(false);  // Player 2 lost
                return;
            }
        }

        // Check for snake 2 colliding with snake 1
        for (Ellipse2D.Double part : player1.getParts()) {
            if (part.getX() == newX2 && part.getY() == newY2) {
                handleGameOver(false);  // Player 2 lost
                return;
            }
        }

        // Check for apple collision for player 2
        if (apple.getShape().intersects(newX2, newY2, 15, 15)) {
            score2++;
            repositionApple();
        } else {
            if (player2.getParts().size() > 5) {
                player2.getParts().remove(player2.getParts().size() - 1);
            }
        }
    }

    private void repositionApple() {
        Random rand = new Random();
        int newX, newY;
        boolean validPosition;

        do {
            // Generate random positions within the panel, excluding the top 50px for the scoreboard
            newX = rand.nextInt(PANEL_WIDTH / 15) * 15;
            newY = rand.nextInt((PANEL_HEIGHT - 60) / 15) * 15 + 60; // Ensure Y is below the scoreboard (starting at Y=40)

            validPosition = true;

            // Check if the apple position intersects with either snake
            for (Ellipse2D.Double part : player1.getParts()) {
                if (part.getX() == newX && part.getY() == newY) {
                    validPosition = false;
                    break;
                }
            }

            for (Ellipse2D.Double part : player2.getParts()) {
                if (part.getX() == newX && part.getY() == newY) {
                    validPosition = false;
                    break;
                }
            }

        } while (!validPosition); // Continue generating until a valid position is found

        apple.setPosition(newX, newY);
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            new Thread(this::gameLoop).start(); // Start the game loop if running is true
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player2.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_S:
                player2.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                player2.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                player2.changeDirection(Direction.RIGHT);
                break;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                player1.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                player1.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                player1.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                player1.changeDirection(Direction.RIGHT);
                break;
        }
    }

    public synchronized void handleGameOver(boolean isPlayer1) {
        running = false; // Stop the game loop
        String message = (isPlayer1 ? "Player 1" : "Player 2") + " has lost! Would you like to restart the game?";
        int response = JOptionPane.showConfirmDialog(snakeFrame, message, "Game Over", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            snakeFrame.resetGame(); // Call the reset method in SnakeFrame to restart the game
        } else {
            System.exit(0); // Exit the game
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}