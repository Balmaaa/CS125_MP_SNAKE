import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
    private int score1 = 0;
    private int score2 = 0;

    public ScorePanel() {
        setPreferredSize(new Dimension(400, 100));
        setBackground(Color.BLACK);
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1 Score: " + score1, 20, 30);
        g.drawString("Player 2 Score: " + score2, 220, 30);
    }
}