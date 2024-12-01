import java.awt.*;
import javax.swing.*;

public class GameMenu extends JFrame {

    public GameMenu() {
        setTitle("Snake Multiplayer - Start Menu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start button to launch the game
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        add(startButton, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startGame() {
        // Launch the game frame
        EventQueue.invokeLater(() -> new SnakeFrame());
        dispose(); // Close the menu after starting the game
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMenu::new);
    }
}
