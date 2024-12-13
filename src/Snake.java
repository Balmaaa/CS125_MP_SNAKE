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
    private boolean isPlayer1;  

    public Snake(GameField gameField, ScorePanel scorePanel, Direction initialDirection, 
                 int upKey, int leftKey, int downKey, int rightKey, boolean isPlayer1) {
        this.gameField = gameField;
        this.scorePanel = scorePanel;
        this.parts = new ArrayList<>();
        this.direction = initialDirection;
        this.isPlayer1 = isPlayer1;

        double startX = 150 + (isPlayer1 ? 160 : 0);
        double startY = 150;

        // Initialize the snake parts
        parts.add(new Ellipse2D.Double(startX, startY, moveSize, moveSize)); 
        for (int i = 1; i < 4; i++) { 
            parts.add(new Ellipse2D.Double(startX, startY + i * moveSize, moveSize, moveSize)); 
        }

        // Add key bindings for movement
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
            System.out.println((isPlayer1 ? "Player 1" : "Player 2") + " changing direction to: " + newDirection);
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

        double newX = parts.get(0).getX();
        double newY = parts.get(0).getY();

        // Update the position based on the current direction
        switch (direction) {
            case UP -> newY -= moveSize;
            case DOWN -> newY += moveSize;
            case LEFT -> newX -= moveSize;
            case RIGHT -> newX += moveSize;
        }

        // Check for wall collisions
        if (newX < 0 || newX >= GameField.PANEL_WIDTH || newY < 0 || newY >= GameField.PANEL_HEIGHT) {
            System.out.println("Game Over: Snake hit the wall!");
            alive = false;
            gameField.handleGameOver(isPlayer1); // Notify GameField of game over
            return;
        }

        // Check for self-collisions
        for (int i = 1; i < parts.size(); i++) {
            if (parts.get(i).getX() == newX && parts.get(i).getY() == newY ) {
                System.out.println("Game Over: Snake collided with itself!");
                alive = false;
                gameField.handleGameOver(isPlayer1); // Notify GameField of game over
                return;
            }
        }

        // Move the snake
        parts.add(0, new Ellipse2D.Double(newX, newY, moveSize, moveSize));

        Apple apple = gameField.getApple();
        if (apple.getShape().intersects(parts.get(0).getBounds2D())) {
            if (isPlayer1) {
                scorePanel.increaseScore1();
            } else {
                scorePanel.increaseScore2(); 
            }
            apple.reposition(); 
        } else {
            parts.remove(parts.size() - 1); // Remove the last part of the snake
        }

        // Update the GameField with the current snake parts
        if (isPlayer1) {
            gameField.setSnakeParts1(parts);
        } else {
            gameField.setSnakeParts2(parts);
        }
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