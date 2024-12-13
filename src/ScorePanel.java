import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
    private int score1 = 0;
    private int score2 = 0;

    public synchronized void increaseScore1() {
        score1++;
        repaint();
    }

    public synchronized void increaseScore2() {
        score2++;
        repaint();
    }

    public synchronized void clear() {
        score1 = 0;
        score2 = 0;
        repaint();
    }
}