import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class Snake {
    private List<Ellipse2D.Double> parts;
    private Direction direction;
    private int moveSize = 20;
    private GameField gameField;
    private ScorePanel scorePanel;
    private boolean alive = true;
    private boolean isPlayer1;  // This will differentiate player 1 and player 2

    // Constructor
    public Snake(GameField gameField, ScorePanel scorePanel, Direction initialDirection, 
                 int upKey, int leftKey, int downKey, int rightKey, boolean isPlayer1) {
        this.gameField = gameField;
        this.scorePanel = scorePanel;
        this.parts = new ArrayList<>();
        this.direction = initialDirection;
        this.isPlayer1 = isPlayer1;

        // Initialize the snake with 4 parts
        for (int i = 0; i < 4; i++) {
            parts.add(new Ellipse2D.Double(100 + (isPlayer1 ? 160 : 0), 100 + i * moveSize, moveSize, moveSize));
        }

        // Add KeyBindings to move the snake
        addKeyBindings(upKey, leftKey, downKey, rightKey);
    }

    // Keybindings for moving the snake
    private void addKeyBindings(int upKey, int leftKey, int downKey, int rightKey) {
        gameField.getInputMap().put(KeyStroke.getKeyStroke(upKey, 0), "moveUp" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveUp" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.UP));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(leftKey, 0), "moveLeft" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveLeft" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.LEFT));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(downKey, 0), "moveDown" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveDown" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.DOWN));

        gameField.getInputMap().put(KeyStroke.getKeyStroke(rightKey, 0), "moveRight" + (isPlayer1 ? "1" : "2"));
        gameField.getActionMap().put("moveRight" + (isPlayer1 ? "1" : "2"), new MoveAction(this, Direction.RIGHT));
    }

    public void changeDirection(Direction newDirection) {
        if (alive) {
            // Log the direction change for the specific player
            if (isPlayer1) {
                System.out.println("Player 1 changing direction to: " + newDirection);
            } else {
                System.out.println("Player 2 changing direction to: " + newDirection);
            }

            this.direction = newDirection;
        }
    }

    public List<Ellipse2D.Double> getParts() {
        return parts;
    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {
        if (!alive) return;
    
        // Move the snake by updating its head position
        double newX = parts.get(0).getX();
        double newY = parts.get(0).getY();
    
        switch (direction) {
            case UP -> newY -= moveSize;
            case DOWN -> newY += moveSize;
            case LEFT -> newX -= moveSize;
            case RIGHT -> newX += moveSize;
        }
    
        // Check for collisions with the walls
        if (newX < 0 || newX >= GameField.PANEL_WIDTH || newY < 0 || newY >= GameField.PANEL_HEIGHT) {
            alive = false;
            return;
        }
    
        // Check for collisions with itself
        for (int i = 1; i < parts.size(); i++) {
            if (parts.get(i).getX() == newX && parts.get(i).getY() == newY) {
                alive = false;
                return;
            }
        }
    
        // Update the parts list (this moves the snake)
        parts.add(0, new Ellipse2D.Double(newX, newY, moveSize, moveSize));
    
        // Check for apple collection
        Apple apple = gameField.getApple();
        if (apple.getShape().intersects(parts.get(0).getBounds2D())) {
            scorePanel.increaseScore1();  // Update this based on the player
            apple.reposition(); // Reposition apple if eaten
        } else {
            parts.remove(parts.size() - 1); // Remove the tail if no apple eaten
        }
    
        // Update the GameField with the current snake parts
        if (isPlayer1) {
            gameField.setSnakeParts1(parts);
        } else {
            gameField.setSnakeParts2(parts);
        }
    }

    // Inner MoveAction class for keybindings
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
