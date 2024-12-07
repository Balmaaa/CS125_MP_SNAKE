public class Game implements Runnable {
    private GameField gameField;
    private Snake player1;
    private Snake player2;
    private SnakeFrame frame;

    public Game(GameField gameField, Snake player1, Snake player2, SnakeFrame frame) {
        this.gameField = gameField;
        this.player1 = player1;
        this.player2 = player2;
        this.frame = frame;
    }

    @Override
    public void run() {
        while (player1.isAlive() && player2.isAlive()) {
            player1.update(); // Move player 1
            player2.update(); // Move player 2
            gameField.repaint(); // Repaint the game field
            try {
                Thread.sleep(100); // Control the speed of the game
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.gameOver();
    }
}