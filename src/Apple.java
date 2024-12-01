import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Apple {
    public static final int SIZE = 20; // Define the size of the apple
    private Ellipse2D.Double shape;
    private Random random = new Random();

    public Apple(int x, int y) {
        shape = new Ellipse2D.Double(x, y, SIZE, SIZE);
    }

    public Ellipse2D.Double getShape() {
        return shape;
    }

    public void reposition() {
        int x = random.nextInt(GameField.PANEL_WIDTH / SIZE) * SIZE;
        int y = random.nextInt(GameField.PANEL_HEIGHT / SIZE) * SIZE;
        shape.setFrame(x, y, SIZE, SIZE);
    }
}