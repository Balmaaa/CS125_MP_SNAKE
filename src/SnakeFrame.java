import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeFrame extends JFrame 
{
    private ScorePanel scorePanel;
    private GameField gameField;
    private Thread gameThread;
    private Snake player1;
    private Snake player2;
    private boolean started = false;

    public SnakeFrame() 
    {
        initComponents();
        initGame();
        initFrame();
    }

    private void initComponents() 
    {
        setLayout(new GridBagLayout());
        scorePanel = new ScorePanel();
        add(scorePanel, new GBC(0, 8, 8, 1));

        gameField = new GameField(this);
        add(gameField, new GBC(0, 0, 8, 8));
    }

    private void initGame() 
    {
        // Create the Snake Objects
        player1 = new Snake(gameField, scorePanel, Direction.UP, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(gameField, scorePanel, Direction.DOWN, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, false);

        // Manage Game Thread
        Runnable gameRunnable = new Game(gameField, player1, player2);
        gameThread = new Thread(gameRunnable);
    }

    private void initFrame() 
    {
        pack();
        setTitle("Snake Multiplayer");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        gameField.requestFocusInWindow();
    }

    public void newGame() 
    {
        if (!started) 
        {
            started = true;
            gameThread.start();
        }
    }

    public void gameOver(String loser) 
    {
        int returnValue = JOptionPane.showConfirmDialog(this, loser + " Lost! \nDo you want to start a new game?", "GAME OVER!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        this.requestFocus();

        if (returnValue == JOptionPane.OK_OPTION) 
        {
            resetGame();
        } 
        else 
        {
            System.exit(0);
        }
    }

    public void resetGame() 
    {
        if (gameThread != null && gameThread.isAlive()) 
        {
            gameThread.interrupt();
        }

        started = false;
        player1.reset();
        player2.reset();
        gameField.initDefaults();
        scorePanel.clear();

        
        Runnable r = new Game(gameField, player1, player2);
        gameThread = new Thread(r);
        gameThread.start();

        gameField.requestFocusInWindow();
        gameField.repaint();
        scorePanel.repaint();
    }

    // Manage Test Case for Pausing Individual Snake Movement
    public void setPlayer1Pause() 
    {
        player1.setPaused(true);
    }

    public void setPlayer1Resume() 
    {
        player1.setPaused(false);
    }

    public void setPlayer2Pause() 
    {
        player2.setPaused(true);
    }

    public void setPlayer2Resume() 
    {
        player2.setPaused(false);
    }
}