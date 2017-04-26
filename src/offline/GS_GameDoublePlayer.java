package offline;

import Models.Food;
import Models.Snake;
import Models.SpecialFood;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/1/18.
 */
public class GS_GameDoublePlayer extends GS_Game{

    private Snake snake1;
    private Snake snake2;
    private Snake[] snakes;

    public GS_GameDoublePlayer() {
        setup();
    }

    public Snake[] getSnakes() {
        return snakes;
    }

    @Override
    protected void setup() {
        snake1 = new Snake(4,5);
        snake2 = new Snake(4,15);
        snakes = new Snake[2];
        snakes[0] = snake1;
        snakes [1] =snake2;
        hasFood = false;
        createFood();
        isOver = false;
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



    protected void createSpecialFood() {
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
    @Override
    protected void createFood() {
        if (!hasFood) {
            food = new Food();
            while (food.eaten(snake1.getHead().x, snake1.getHead().y) || food.eaten(snake2.getHead().x,snake2.getHead().y)) {
                food = new Food();
            }
            hasFood = true;
        }
    }

    protected void checkCollision(Snake s) {
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


}
