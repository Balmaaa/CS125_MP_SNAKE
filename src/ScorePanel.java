import javax.swing.JPanel;

public class ScorePanel extends JPanel 
{
    int score1 = 0;
    int score2 = 0;

    public synchronized void increaseScore1() 
    {
        score1++;
        repaint();
    }

    public synchronized void increaseScore2() 
    {
        score2++;
        repaint();
    }

    public synchronized void clear()
    {
        score1 = 0;
        score2 = 0;
        repaint();
    }
}