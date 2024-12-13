import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Apple {
    public static final int SIZE = 20; // Size of the apple
    private Ellipse2D.Double shape;
    private Random random;

    public Apple(int x, int y) {
        this.shape = new Ellipse2D.Double(x, y, SIZE, SIZE); // Initialize the apple shape
        this.random = new Random();
    }

    public Ellipse2D.Double getShape() {
        return shape;
    }

    // Method to set the position of the apple
    public void setPosition(int x, int y) {
        shape.setFrame(x, y, SIZE, SIZE); // Update the position of the apple
    }

    // Method to randomly relocate the apple within the game field
    public void relocate() {
        int x = random.nextInt((GameField.PANEL_WIDTH / SIZE)) * SIZE; // Random x position within the game field
        int y = random.nextInt((GameField.PANEL_HEIGHT / SIZE)) * SIZE; // Random y position within the game field
        setPosition(x, y); // Update the position of the apple
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D
        g2d.setColor(Color.RED); // Set the color to red for the apple
        g2d.fill(shape); // Fill the apple shape with the color
    }
}