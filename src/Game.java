public class Game implements Runnable 
{
    private GameField gameField;
    private Snake player1;
    private Snake player2;
    private boolean running = true;
    private boolean canMovePlayer1 = true;
    private boolean canMovePlayer2 = true;

    public Game(GameField gameField, Snake player1, Snake player2) 
    {
        this.gameField = gameField;
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() 
    {
        // Player 1 Thread
        Thread player1Thread = new Thread(() -> {
            while (running) 
            {
                if (canMovePlayer1) 
                {
                    player1.move();
                }
                try 
                {
                    Thread.sleep(150);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        });

        // Player 2 Thread
        Thread player2Thread = new Thread(() -> {
            while (running) 
            {
                if (canMovePlayer2)
                {
                    player2.move();
                }
                try 
                {
                    Thread.sleep(150);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        });

        // Start Snake Threads
        player1Thread.start();
        player2Thread.start();

        try 
        {
            player1Thread.join();
            player2Thread.join();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    public void setPlayer1Pause() { canMovePlayer1 = false; }

    public void setPlayer1Resume() { canMovePlayer1 = true; }

    public void setPlayer2Pause() { canMovePlayer2 = false; }

    public void setPlayer2Resume() { canMovePlayer2 = true; }

    public void stopGame() { running = false; }
}