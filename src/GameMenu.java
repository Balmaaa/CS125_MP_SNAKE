import java.awt.*;
import javax.swing.*;

public class GameMenu extends JFrame
{

    private Image backgroundImage;

    public GameMenu()
    {
        setTitle("Snake Multiplayer");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(Box.createVerticalStrut(40));

        JLabel titleLabel = new JLabel("SNAKE MULTIPLAYER", JLabel.CENTER);
        titleLabel.setFont(new Font("Lexend", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        centerPanel.add(Box.createVerticalStrut(20));

        JLabel subtitleLabel = new JLabel("Press Start To Play", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.BLACK);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);

        centerPanel.add(Box.createVerticalStrut(30));

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> startGame());
        centerPanel.add(startButton);

        panel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(panel);
        setVisible(true);
    }

    private void startGame()
    {
        EventQueue.invokeLater(() -> new SnakeFrame());
        dispose();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (backgroundImage != null)
        {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(GameMenu::new);
    }
}