package model;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/1/18.
 */
public class GS_GameDoublePlayer {

    public static final int COLUMNS = 26;
    public static final int ROWS = 26;
    public static final int HEIGHT = COLUMNS * Snake.BODY_SIZE;
    public static final int WIDTH = ROWS * Snake.BODY_SIZE;
    private Snake snake1;
    private Snake snake2;
    private Snake[] snakes;
    private Food food;
    private SpecialFood specialFood;
    private boolean isOver;
    private boolean hasFood;
    private boolean hasSpecialFood = false;

    public GS_GameDoublePlayer() {
        setup();
    }

    public Snake[] getSnakes() {
        return snakes;
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
        snake1 = new Snake(4,5);
        snake2 = new Snake(4,15);
        snakes = new Snake[2];
        snakes[0] = snake1;
        snakes [1] =snake2;
        hasFood = false;
        createFood();
        isOver = false;
       // System.out.print("setup finish");
    }

    public void update() {
        snake1.move();
        snake2.move();
        checkCollision(snake1);
        checkCollision(snake2);
        checkEat(snake2);
        checkEat(snake1);
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
                while (specialFood.eaten(snake1.getHead().x, snake1.getHead().y) || specialFood.equals(food))
                    specialFood = new SpecialFood();
                hasSpecialFood = true;
            }
        }
    }

    private void createFood() {
        if (!hasFood) {
            food = new Food();
            while (food.eaten(snake1.getHead().x, snake1.getHead().y)) {
                food = new Food();
            }
            hasFood = true;
        }
    }

    private void checkCollision(Snake s) {
        List<Snake.Body> longBody1 = snake1.getBodyList();
        List<Snake.Body> longBody2 = snake2.getBodyList();
        Snake.Body head = s.getHead();
        if (head.x >= COLUMNS - 1 || head.y >= ROWS - 1 || head.x <= 0 || head.y <= 0) {
            isOver = true;
            return;
        }

        for (Snake.Body body : longBody1) {
            if (head.equals(body) && head != body) {
                isOver = true;
                break;
            }
        }
        for (Snake.Body body : longBody2) {
            if (head.equals(body) && head != body) {
                isOver = true;
                break;
            }
        }
    }

    public void keyPressed(int KeyCode) {
        switch (KeyCode) {
            case KeyEvent.VK_UP:
                if (!snake1.getFacing().equals("up") && !snake1.getFacing().equals("down"))
                    snake1.setFacing("up");
                break;
            case KeyEvent.VK_DOWN:
                if (!snake1.getFacing().equals("up") && !snake1.getFacing().equals("down"))
                    snake1.setFacing("down");
                break;
            case KeyEvent.VK_RIGHT:
                if (!snake1.getFacing().equals("right") && !snake1.getFacing().equals("left"))
                    snake1.setFacing("right");
                break;
            case KeyEvent.VK_LEFT:
                if (!snake1.getFacing().equals("right") && !snake1.getFacing().equals("left"))
                    snake1.setFacing("left");
                break;
            case KeyEvent.VK_W:
                if (!snake2.getFacing().equals("up") && !snake2.getFacing().equals("down"))
                    snake2.setFacing("up");
                break;
            case KeyEvent.VK_S:
                if (!snake2.getFacing().equals("up") && !snake2.getFacing().equals("down"))
                    snake2.setFacing("down");
                break;
            case KeyEvent.VK_D:
                if (!snake2.getFacing().equals("right") && !snake2.getFacing().equals("left"))
                    snake2.setFacing("right");
                break;
            case KeyEvent.VK_A:
                if (!snake2.getFacing().equals("right") && !snake2.getFacing().equals("left"))
                    snake2.setFacing("left");
                break;
            case KeyEvent.VK_R:
                setup();
        }


    }

    private void checkEat(Snake s) {
        if (food.eaten(s.getHead().x, s.getHead().y)) {
            hasFood = false;
            snake1.grow();
        }
        if (specialFood != null && specialFood.eaten(s.getHead().x, s.getHead().y)) {
            hasSpecialFood = false;
            if (specialFood.decay > 3) {
                snake1.grow();
                snake1.grow();
            }
            specialFood = null;
        }
    }
}
