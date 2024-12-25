import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

public class GameField extends JPanel implements KeyListener {
    public static final int PANEL_WIDTH = 600;
    public static final int PANEL_HEIGHT = 600;
    private Apple apple;
    private boolean running;
    private Snake player1;
    private Snake player2;
    private int score1;
    private int score2;
    private ScorePanel scorePanel;
    private SnakeFrame snakeFrame;

    public GameField(SnakeFrame snakeFrame) {
        this.snakeFrame = snakeFrame;
        this.scorePanel = new ScorePanel();
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        initDefaults();
        setFocusable(true);
        addKeyListener(this);
        running = true;
    }

    public void setRunning(boolean running) { this.running = running; }

    public void initDefaults() 
    {
        apple = new Apple(300, 300);
        player1 = new Snake(this, scorePanel, Direction.RIGHT, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(this, scorePanel, Direction.LEFT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, false);
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(240, 240, 240, 50));
        for (int i = 0; i < PANEL_WIDTH; i += 15) {
            g2.drawLine(i, 0, i, PANEL_HEIGHT);
        }
        for (int i = 0; i < PANEL_HEIGHT; i += 15) {
            g2.drawLine(0, i, PANEL_WIDTH, i);
        }

        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(0, 0, PANEL_WIDTH, 60);

        g2.setPaint(Color.RED);
        g2.fill(apple.getShape());

        g2.setPaint(new Color(34, 136, 215));
        for (Ellipse2D e : player1.getParts()) { g2.fill(e); }

        g2.setPaint(new Color(255, 215, 0));
        for (Ellipse2D e : player2.getParts()) { g2.fill(e); }

        g2.setPaint(Color.WHITE);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g2.drawString("Player 1: " + score1, 30, 40);
        g2.drawString("Player 2: " + score2, PANEL_WIDTH - 180, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        // Player 1 Controls
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player1.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_S:
                player1.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                player1.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                player1.changeDirection(Direction.RIGHT);
                break;
        }

        // Player 2 Controls
        switch (e.getKeyCode()) 
        {
            case KeyEvent.VK_UP:
                player2.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                player2.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                player2.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                player2.changeDirection(Direction.RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}