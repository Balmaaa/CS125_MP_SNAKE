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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameField extends JPanel implements KeyListener {

    public static final int PANEL_WIDTH = 600;
    public static final int PANEL_HEIGHT = 600;
    private List<Ellipse2D.Double> snakeParts1;
    private List<Ellipse2D.Double> snakeParts2;
    private Apple apple;
    private SnakeFrame snakeFrame;

    // Player 1 and 2 Snake Movement
    private int snake1Direction;
    private int snake2Direction;
    private boolean running;
    // Player 1 and 2 Scoreboards
    private int score1;
    private int score2;

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

    public Apple getApple() {
        return this.apple; 
    }
    public void setSnakeParts1(List<Ellipse2D.Double> snakeParts1) {
        this.snakeParts1 = snakeParts1; 
    }
    public void setSnakeParts2(List<Ellipse2D.Double> snakeParts2) {
        this.snakeParts2 = snakeParts2; 
    }

    
    

    public void initDefaults()
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

        // Player 21 Snake
        snakeParts2.add(new Ellipse2D.Double(100, 100, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 120, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 140, 20, 20));
        snakeParts2.add(new Ellipse2D.Double(100, 160, 20, 20));
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

        // Snake 1 Design
        g2.setPaint(Color.RED);
        g2.fill(apple.getShape());

        g2.setPaint(new Color(34, 136, 215));
        for (Ellipse2D e : snakeParts1)
        {
            g2.fill(e);
        }

        // Snake 2 Design
        g2.setPaint(new Color(255, 215, 0));
        for (Ellipse2D e : snakeParts2)
        {
            g2.fill(e);
        }

        // Scoreboard Design
        g2.setPaint(Color.WHITE);
        g2.drawString("Player 1 Score: " + score1, 10, 20);
        g2.drawString("Player 2 Score: " + score2, 10, 40);
    }

    private void gameLoop() 
    {
        while (running) 
        {
            moveSnake1();
            moveSnake2();
            repaint();
            try 
            {
                Thread.sleep(100);
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

        if (newX < 0 || newX >= PANEL_WIDTH || newY < 0 || newY >= PANEL_HEIGHT)
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


        if (apple.getShape().intersects(newX, newY, 20, 20))
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

        snakeParts1.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
    }

    private void moveSnake2()
    {
        Ellipse2D.Double head = snakeParts2.get(0);
        double newX = head.getX();
        double newY = head.getY();

        switch (snake2Direction)
        {
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

        if (newX < 0 || newX >= PANEL_WIDTH || newY < 0 || newY >= PANEL_HEIGHT)
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

        if (apple.getShape().intersects(newX, newY, 20, 20))
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
        snakeParts2.add(0, new Ellipse2D.Double(newX, newY, 20, 20));
    }

    private void repositionApple()
    {
        Random rand = new Random();
        int newX, newY;
        boolean validPosition;

        do 
        {
            newX = rand.nextInt(PANEL_WIDTH / 20) * 20;
            newY = rand.nextInt(PANEL_HEIGHT / 20) * 20;
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
        } 
        while (!validPosition);

        apple.setPosition(newX, newY);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode()) 
        {
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

        switch (e.getKeyCode()) 
        {
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

    public void handleGameOver(boolean isPlayer1) 
    {
        String loser = isPlayer1 ? "Player 1" : "Player 2";
        System.out.println(loser + " has lost!");
        
        SwingUtilities.invokeLater(() -> snakeFrame.gameOver(loser));

        running = false;
    }


    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}