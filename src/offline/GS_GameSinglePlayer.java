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
public class GS_GameSinglePlayer extends GS_Game {

    public GS_GameSinglePlayer() {
        setup();
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

    protected void createSpecialFood() {
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
}
