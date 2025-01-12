import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeFrame extends JFrame {
    private ScorePanel scorePanel;
    private GameField gameField;
    private Thread gameThread;
    private Snake player1;
    private Snake player2;
    private boolean started = false;

    public SnakeFrame() {
        initComponents();
        initGame();
        initFrame();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        addKeyListener(new KeyboardHandler());
        scorePanel = new ScorePanel();
        add(scorePanel, new GBC(0, 8, 8, 1));

        gameField = new GameField(this); // Pass the reference of SnakeFrame to GameField
        add(gameField, new GBC(0, 0, 8, 8));
    }

    private void initGame() {
        player1 = new Snake(gameField, scorePanel, Direction.UP, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, true);
        player2 = new Snake(gameField, scorePanel, Direction.DOWN, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, false);
        Runnable gameRunnable = new Game(gameField, player1, player2, this);
        gameThread = new Thread(gameRunnable);
        gameField.requestFocusInWindow(); 
    }

    private void initFrame() {
        pack();
        setTitle("Snake Multiplayer");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void newGame() {
        if (!started) {
            started = true;
            gameThread.start(); 
        }
    }

    public void gameOver(String loser) {
        int returnValue = JOptionPane.showConfirmDialog(this,
                loser + " Lost! \nDo you want to start a new game?", "GAME OVER!", JOptionPane
                        .OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        this.requestFocus();

        if (returnValue == JOptionPane.OK_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    public void resetGame() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();  // Interrupt the previous game thread
        }
    
        // Reset game state
        started = false;
    
        // Reset both players' snakes
        player1.reset();
        player2.reset();
    
        // Reset the game field and other components
        gameField.initDefaults();
        scorePanel.clear();  // Reset the scores
    
        // Set the running state to true and start the game loop
        gameField.setRunning(true); // Ensure the running flag is set to true
    
        // Start the game
        Runnable r = new Game(gameField, player1, player2, this);
        gameThread = new Thread(r);  // Create a new game thread
    
        gameThread.start();  // Start the new game
    
        // Focus on the game field so it can capture key events
        gameField.requestFocusInWindow();
    
        // Repaint the game components (game field, score panel)
        gameField.repaint();
        scorePanel.repaint();
    }

    private class KeyboardHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!started) newGame();
        }
    }
}