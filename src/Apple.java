import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Apple 
{
    public static final int SIZE = 20; // Apple Size
    private Ellipse2D.Double shape;
    private Random random;

    public Apple(int x, int y) 
    {
        this.shape = new Ellipse2D.Double(x, y, SIZE, SIZE);
        this.random = new Random();
    }

    public Ellipse2D.Double getShape() 
    {
        return shape;
    }

    // Position of the apple
    public void setPosition(int x, int y) 
    {
        shape.setFrame(x, y, SIZE, SIZE); // Update Apple
    }

    // Change and Update Apple's Location
    public void relocate() 
    {
        int x = random.nextInt((GameField.PANEL_WIDTH / SIZE)) * SIZE;
        int y = random.nextInt((GameField.PANEL_HEIGHT / SIZE)) * SIZE;
        setPosition(x, y); // Update Position
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fill(shape);
    }
}