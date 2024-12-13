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
        addKeyListener(new KeyboardHandler());
        scorePanel = new ScorePanel();
        add(scorePanel, new GBC(0, 8, 8, 1));

        gameField = new GameField(this);
        add(gameField, new GBC(0, 0, 8, 8));
    }

    private void initGame() 
    {
        
        player1 = new Snake(gameField, scorePanel, Direction.UP, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(gameField, scorePanel, Direction.DOWN, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, false);
        Runnable gameRunnable = new Game(gameField, player1, player2, this);
        gameThread = new Thread(gameRunnable);
        gameField.requestFocusInWindow(); 
    }

    private void initFrame() 
    {
        pack();
        setTitle("Snake Multiplayer");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    
    public void newGame() 
    {
        if (!started) 
        {
            started = true;
            gameThread.start(); 
        }
    }

    public void gameOver() 
    {
        int returnValue = JOptionPane.showConfirmDialog(this,
                "Do you want to start a new game?", "GAME OVER!", JOptionPane
                        .OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        this.requestFocus();

        switch (returnValue) 
        {
            case JOptionPane.OK_OPTION:
                resetGame();
                break;
            case JOptionPane.CANCEL_OPTION:
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(getParent(),
                        "Something went wrong :( /n Please relaunch app");
                break;
        }
    }

    private void resetGame()
    {
        started = false;
        
        player1 = new Snake(gameField, scorePanel, Direction.UP, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(gameField, scorePanel, Direction.DOWN, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, false);
        
        scorePanel.clear();
        gameField.initDefaults(); 
        
        scorePanel.repaint();
        gameField.repaint();
        
        Runnable r = new Game(gameField, player1, player2, this);
        gameThread = new Thread(r);
        gameField.requestFocusInWindow();
    }

    private class KeyboardHandler extends KeyAdapter 
    {
        @Override
        public void keyPressed(KeyEvent e) 
        {
            if (!started) newGame();
        }
    }
}

