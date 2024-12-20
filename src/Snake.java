import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

public class Snake {
    private List<Ellipse2D.Double> parts;
    private Direction direction;
    private int moveSize = 15;
    private GameField gameField;
    private ScorePanel scorePanel; // Keep this for score tracking
    private boolean alive = true;  
    private boolean isPlayer1;  

    public Snake(GameField gameField, ScorePanel scorePanel, Direction initialDirection, 
                 int upKey, int leftKey, int downKey, int rightKey, boolean isPlayer1) {
        this.gameField = gameField;
        this.scorePanel = scorePanel; // Initialize score panel
        this.parts = new ArrayList<>();
        this.direction = initialDirection;
        this.isPlayer1 = isPlayer1;

        double startX = 150 + (isPlayer1 ? 160 : 0);
        double startY = 150;

        // Snake Parts
        parts.add(new Ellipse2D.Double(startX, startY, moveSize, moveSize)); 
        for (int i = 1; i < 4; i++) { 
            parts.add(new Ellipse2D.Double(startX, startY + i * moveSize, moveSize, moveSize)); 
        }

        // Keybinds
        addKeyBindings(upKey, leftKey, downKey, rightKey);
    }

    private void addKeyBindings(int upKey, int leftKey, int downKey, int rightKey) {
        gameField.getInputMap().put(KeyStroke.getKeyStroke(upKey, 0), "moveUp" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveUp" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.UP));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(leftKey, 0), "moveLeft" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveLeft" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.LEFT));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(downKey, 0), "moveDown" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveDown" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.DOWN));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(rightKey, 0), "moveRight" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveRight" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.RIGHT));

        gameField.requestFocusInWindow(); 
    }

    public void changeDirection(Direction newDirection) {
        if (alive) {
            // Prevent the snake from reversing direction
            if ((this.direction == Direction.UP && newDirection != Direction.DOWN) ||
                (this.direction == Direction.DOWN && newDirection != Direction.UP) ||
                (this.direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
                (this.direction == Direction.RIGHT && newDirection != Direction.LEFT)) {
                this.direction = newDirection;
            }
        }
    }

    public void move() {
        if (alive) {
            // Move the snake by adding a new head in the direction of movement
            double headX = parts.get(0).getX();
            double headY = parts.get(0).getY();

            switch (direction) {
                case UP:
                    headY -= moveSize;
                    break;
                case DOWN:
                    headY += moveSize;
                    break;
                case LEFT:
                    headX -= moveSize;
                    break;
                case RIGHT:
                    headX += moveSize;
                    break;
            }

            // Add new head
            parts.add(0, new Ellipse2D.Double(headX, headY, moveSize, moveSize));

            // Remove the tail
            parts .remove(parts.size() - 1);
        }
    }

    public List<Ellipse2D.Double> getParts() {
        return parts;
    }

    public boolean isAlive() {
        return alive;
    }

    public void reset() {
        parts.clear(); // Clear any existing parts
        alive = true; // Reset the alive status
        direction = Direction.UP; // Reset to initial direction
        double startX = 150 + (isPlayer1 ? 160 : 0);
        double startY = 150;

        // Reinitialize snake parts
        parts.add(new Ellipse2D.Double(startX, startY, moveSize, moveSize)); 
        for (int i = 1; i < 4; i++) { 
            parts.add(new Ellipse2D.Double(startX, startY + i * moveSize, moveSize, moveSize)); 
        }
    }

    public void moveSnake() {
        move();
    }

    private class MoveAction extends AbstractAction {
        private Snake snake;
        private Direction direction;

        public MoveAction(Snake snake, Direction direction) {
            this.snake = snake;
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            snake.changeDirection(direction);
        }
    }
}