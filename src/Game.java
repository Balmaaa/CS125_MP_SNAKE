import javax.swing.SwingUtilities;

public class Game implements Runnable
{
    private GameField gameField;
    private Snake player1;
    private Snake player2;
    private SnakeFrame frame;

    public Game(GameField gameField, Snake player1, Snake player2, SnakeFrame frame)
    {
        this.gameField = gameField;
        this.player1 = player1;
        this.player2 = player2;
        this.frame = frame;
    }

    @Override
    public void run() {
        
        while (player1.isAlive() && player2.isAlive()) {
            player1.update();
            player2.update();
            gameField.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String loser = player1.isAlive() ? "Player 1" : "Player 2";
        SwingUtilities.invokeLater(() -> frame.gameOver(loser));
    }
}
