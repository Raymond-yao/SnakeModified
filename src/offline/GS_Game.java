package offline;

import Models.Food;
import Models.Snake;
import Models.SpecialFood;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/3/24.
 */
public abstract class GS_Game {
    public static final int COLUMNS = 26;
    public static final int ROWS = 26;
    public static final int HEIGHT = COLUMNS * Snake.BODY_SIZE;
    public static final int WIDTH = ROWS * Snake.BODY_SIZE;
    protected Snake snake;
    protected Food food;
    protected SpecialFood specialFood;
    protected boolean isOver;
    protected boolean hasFood;
    protected boolean hasSpecialFood = false;

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public SpecialFood getSpecialFood() {
        return specialFood;
    }

    public boolean isOver() {
        return isOver;
    }

    protected void setup() {
        snake = new Snake();
        hasFood = false;
        createFood();
        isOver = false;
    }

    public void update() {
        snake.move();
        checkCollision(snake);
        checkEat(snake);
        createFood();
        createSpecialFood();
        decaySpecialFood();
    }

    protected abstract void createSpecialFood();

    protected void decaySpecialFood() {
        if (specialFood != null) {
            specialFood.decay();
            if (specialFood.decay <= 0) {
                specialFood = null;
                hasSpecialFood = false;
            }
        }
    }


    protected void createFood() {
        if (!hasFood) {
            food = new Food();
            while (food.eaten(snake.getHead().x, snake.getHead().y)) {
                food = new Food();
            }
            hasFood = true;
        }
    }

    protected void checkCollision(Snake s) {
        List<Snake.Body> longBody = snake.getBodyList();
        Snake.Body head = s.getHead();
        if (head.x >= COLUMNS - 1 || head.y >= ROWS - 1 || head.x <= 0 || head.y <= 0) {
            isOver = true;
            return;
        }

        for (Snake.Body body : longBody) {
            if (head.equals(body) && head != body) {
                isOver = true;
                break;
            }
        }
    }

    public abstract void keyPressed(int KeyCode);


    protected void checkEat(Snake s) {
        if (food.eaten(s.getHead().x, s.getHead().y)) {
            hasFood = false;
            s.grow();
        }
        if (specialFood != null && specialFood.eaten(s.getHead().x, s.getHead().y)) {
            hasSpecialFood = false;
            if (specialFood.decay > 3) {
                s.grow();
                s.grow();
            }
            specialFood = null;
        }
    }
}
