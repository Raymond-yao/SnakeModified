package model;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/1/18.
 */
public class GS_Game {

    public static final int COLUMNS = 26;
    public static final int ROWS = 26;
    public static final int HEIGHT = COLUMNS * Snake.BODY_SIZE;
    public static final int WIDTH = ROWS * Snake.BODY_SIZE;
    private Snake snake;
    private Food food;
    private SpecialFood specialFood;
    private boolean isOver;
    private boolean hasFood;
    private boolean hasSpecialFood = false;

    public GS_Game() {
        setup();
    }

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

    private void setup() {
        snake = new Snake();
        hasFood = false;
        createFood();
        isOver = false;
        System.out.print("setup finish");
    }

    public void update() {
        snake.move();
        checkCollision();
        checkEat();
        createFood();
        createSpecialFood();
        decaySpecialFood();
    }

    private void decaySpecialFood() {
        if (specialFood != null) {
            specialFood.decay();
            if (specialFood.decay <= 0) {
                specialFood = null;
                hasSpecialFood = false;
            }
        }
    }

    private void createSpecialFood() {
        if (!hasSpecialFood) {
            int probability = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            if (probability == 2) {
                specialFood = new SpecialFood();
                while (specialFood.eaten(snake.getHead().x, snake.getHead().y) || specialFood.equals(food))
                    specialFood = new SpecialFood();
                hasSpecialFood = true;
            }
        }
    }

    private void createFood() {
        if (!hasFood) {
            food = new Food();
            while (food.eaten(snake.getHead().x, snake.getHead().y)) {
                food = new Food();
            }
            hasFood = true;
        }
    }

    private void checkCollision() {
        List<Snake.Body> longBody = snake.getBodyList();
        Snake.Body head = snake.getHead();
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

    public void keyPressed(int KeyCode) {
        switch (KeyCode) {
            case KeyEvent.VK_UP:
                if (!snake.getFacing().equals("up") && !snake.getFacing().equals("down"))
                    snake.setFacing("up");
                break;
            case KeyEvent.VK_DOWN:
                if (!snake.getFacing().equals("up") && !snake.getFacing().equals("down"))
                    snake.setFacing("down");
                break;
            case KeyEvent.VK_RIGHT:
                if (!snake.getFacing().equals("right") && !snake.getFacing().equals("left"))
                    snake.setFacing("right");
                break;
            case KeyEvent.VK_LEFT:
                if (!snake.getFacing().equals("right") && !snake.getFacing().equals("left"))
                    snake.setFacing("left");
                break;
            case KeyEvent.VK_R:
                setup();
        }


    }

    private void checkEat() {
        if (food.eaten(snake.getHead().x, snake.getHead().y)) {
            hasFood = false;
            snake.grow();
        }
        if (specialFood != null && specialFood.eaten(snake.getHead().x, snake.getHead().y)) {
            hasSpecialFood = false;
            if (specialFood.decay > 3) {
                snake.grow();
                snake.grow();
            }
            specialFood = null;
        }
    }
}
