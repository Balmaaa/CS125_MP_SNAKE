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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameField extends JPanel implements KeyListener 
{

    public static final int PANEL_WIDTH = 600;
    public static final int PANEL_HEIGHT = 600;
    private List<Ellipse2D.Double> snakeParts1;
    private List<Ellipse2D.Double> snakeParts2;
    private Apple apple;
    private int snake1Direction;
    private int snake2Direction;
    private boolean running;
    private int score1;
    private int score2;
    private SnakeFrame snakeFrame;

    public GameField(SnakeFrame snakeFrame) 
    {
        this.snakeFrame = snakeFrame;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        initDefaults();
        setFocusable(true);
        addKeyListener(this);
        running = true;
        new Thread(this::gameLoop).start();
    }

    public synchronized void initDefaults() 
    {
        apple = new Apple(300, 300);
        snakeParts1 = Collections.synchronizedList(new ArrayList<>());
        snakeParts2 = Collections.synchronizedList(new ArrayList<>());

        // Player 1 Snake
        snakeParts1.add(new Ellipse2D.Double(400, 300, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 320, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 340, 20, 20));
        snakeParts1.add(new Ellipse2D.Double(400, 360, 20, 20));
        snake1Direction = 3;

        // Player 2 Snake
        snakeParts2.add(new Ellipse2D.Double(150, 150, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(150, 120, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(150, 140, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(150, 160, 20, 20));
        snake2Direction = 3;

        // Player Scoreboard
        score1 = 0;
        score2 = 0;
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(240, 240, 240, 50));
        for (int i = 0; i < PANEL_WIDTH; i += 15) 
        {
            g2.drawLine(i, 0, i, PANEL_HEIGHT);
        }
        for (int i = 0; i < PANEL_HEIGHT; i += 15) 
        {
            g2.drawLine(0, i, PANEL_WIDTH, i);
        }

        // Draw a solid background for the scoreboard area (top 50px)
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(0, 0, PANEL_WIDTH, 60);

        // Draw Apple
        g2.setPaint(Color.RED);
        g2.fill(apple.getShape());

        // Draw Snake 1
        g2.setPaint(new Color(34, 136, 215));
        for (Ellipse2D e : snakeParts1) {
            g2.fill(e);
        }

        // Draw Snake 2
        g2.setPaint(new Color(255, 215, 0));
        for (Ellipse2D e : snakeParts2) {
            g2.fill(e);
        }

        // Draw Scoreboard
        g2.setPaint(Color.WHITE);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g2.drawString("Player 1: " + score1, 30, 40);
        g2.drawString("Player 2: " + score2, PANEL_WIDTH - 180, 40);
    }

    public Apple getApple() { return apple; }

    public void setSnakeParts1(List<Ellipse2D.Double> parts) { this.snakeParts1 = parts; }

    public void setSnakeParts2(List<Ellipse2D.Double> parts) { this.snakeParts2 = parts; }

    private void gameLoop() 
    {
        while (running) {
            moveSnake1();
            moveSnake2();
            repaint();
            try 
            {
                Thread.sleep(150);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
    }

    private void moveSnake1() 
    {
        Ellipse2D.Double head = snakeParts1.get(0);
        double newX = head.getX();
        double newY = head.getY();

        switch (snake1Direction) 
        {
            case 0: // Up
                newY -= 15;
                break;
            case 1: // Down
                newY += 15;
                break;
            case 2: // Left
                newX -= 15;
                break;
            case 3: // Right
                newX += 15;
                break;
        }

        if (newX < 0 || newX >= PANEL_WIDTH || newY < 50 || newY >= PANEL_HEIGHT) 
        {
            handleGameOver(true);
            return;
        }

        for (int i = 1; i < snakeParts1.size(); i++) 
        {
            if (snakeParts1.get(i).getX() == newX && snakeParts1.get(i).getY() == newY) 
            {
                handleGameOver(true);
                return;
            }
        }

        if (apple.getShape().intersects(newX, newY, 15, 15)) 
        {
            score1++;
            repositionApple();
        } 
        else 
        {
            if (snakeParts1.size() > 5) 
            {
                snakeParts1.remove(snakeParts1.size() - 1);
            }
        }

        snakeParts1.add(0, new Ellipse2D.Double(newX, newY, 15, 15));
    }

    private void moveSnake2() 
    {
        Ellipse2D.Double head = snakeParts2.get(0);
        double newX = head.getX();
        double newY = head.getY();

        switch (snake2Direction) 
        {
            case 0: // Up
                newY -= 15;
                break;
            case 1: // Down
                newY += 15;
                break;
            case 2: // Left
                newX -= 15;
                break;
            case 3: // Right
                newX += 15;
                break;
        }

        if (newX < 0 || newX >= PANEL_WIDTH || newY < 50 || newY >= PANEL_HEIGHT) 
        {
            handleGameOver(false);
            return;
        }

        for (int i = 1; i < snakeParts2.size(); i++) 
        {
            if (snakeParts2.get(i).getX() == newX && snakeParts2.get(i).getY() == newY) 
            {
                handleGameOver(false);
                return;
            }
        }

        if (apple.getShape().intersects(newX, newY, 15, 15)) 
        {
            score2++;
            repositionApple();
        } 
        else 
        {
            if (snakeParts2.size() > 5) 
            {
                snakeParts2.remove(snakeParts2.size() - 1);
            }
        }

        snakeParts2.add(0, new Ellipse2D.Double(newX, newY, 15, 15));
    }

    private void repositionApple() 
    {
        Random rand = new Random();
        int newX, newY;
        boolean validPosition;

        do 
        {
            newX = rand.nextInt(PANEL_WIDTH / 15) * 15;
            newY = rand.nextInt((PANEL_HEIGHT - 60) / 15) * 15 + 60;

            validPosition = true;

            for (Ellipse2D.Double part : snakeParts1)
             {
                if (part.getX() == newX && part.getY() == newY) 
                {
                    validPosition = false;
                    break;
                }
            }

            for (Ellipse2D.Double part : snakeParts2)
             {
                if (part.getX() == newX && part.getY() == newY) 
                {
                    validPosition = false;
                    break;
                }
            }

        } while (!validPosition);

        apple.setPosition(newX, newY);
    }

    public void setRunning(boolean running) 
    {
        this.running = running;
        if (running) 
        {
            new Thread(this::gameLoop).start();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_W:
                if (snake2Direction != 1) snake2Direction = 0; // Up
                break;
            case KeyEvent.VK_S:
                if (snake2Direction != 0) snake2Direction = 1; // Down
                break;
            case KeyEvent.VK_A:
                if (snake2Direction != 3) snake2Direction = 2; // Left
                break;
            case KeyEvent.VK_D:
                if (snake2Direction != 2) snake2Direction = 3; // Right
                break;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (snake1Direction != 1) snake1Direction = 0; // Up
                break;
            case KeyEvent.VK_DOWN:
                if (snake1Direction != 0) snake1Direction = 1; // Down
                break;
            case KeyEvent.VK_LEFT:
                if (snake1Direction != 3) snake1Direction = 2; // Left
                break;
            case KeyEvent.VK_RIGHT:
                if (snake1Direction != 2) snake1Direction = 3; // Right
                break;
        }
    }

    public synchronized void handleGameOver(boolean isPlayer1) 
    {
        running = false;
        String message = (isPlayer1 ? "Player 1" : "Player 2") + " has lost! Would you like to restart the game?";
        int response = JOptionPane.showConfirmDialog(snakeFrame, message, "Game Over", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) 
        {
            snakeFrame.resetGame(); // Restart Game
        } 
        else 
        {
            System.exit(0); // Exit Game
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}