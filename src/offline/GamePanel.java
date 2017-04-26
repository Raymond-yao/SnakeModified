package offline;

import Models.Food;
import Models.Snake;
import Models.SpecialFood;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/3/24.
 */
public class GamePanel extends JPanel {
    private static final String OVER = "Game Over!";
    private static final String REPLAY = "R to replay";
    protected GS_Game game;
    private Color foodColor;

    public GamePanel(GS_Game game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);

    }

    private void drawGame(Graphics g) {
        drawSnake(g);
        drawFood(g);
    }

    private void drawFood(Graphics g) {
        Food food = game.getFood();
        foodColor = new Color(237, 237, 58);
        g.setColor(foodColor);
        g.fillOval(food.getX() * Food.FOOD_SIZE, food.getY() * Food.FOOD_SIZE, Food.FOOD_SIZE, Food.FOOD_SIZE);

        if (game.getSpecialFood() != null) {
            SpecialFood sf = game.getSpecialFood();
            g.setColor(sf.decayColor);
            g.fillRect(sf.getX() * Food.FOOD_SIZE, sf.getY() * Food.FOOD_SIZE, Food.FOOD_SIZE, Food.FOOD_SIZE);
        }
    }

    private void drawSnake(Graphics g) {
        Snake snake = game.getSnake();
        int red;
        int green;
        int blue;
        Color snakecolor;

        for (int i = 0; i < snake.getBodyList().size(); i++) {
            red = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            green = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            blue = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            snakecolor = new Color(red, green, blue);
            g.setColor(snakecolor);
            g.fillRect(snake.getBodyX(i) * Snake.BODY_SIZE, snake.getBodyY(i) * Snake.BODY_SIZE, Snake.BODY_SIZE, Snake.BODY_SIZE);

        }

    }

    private void gameOver(Graphics g) {
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER, g, fm, GS_Game.HEIGHT / 2);
        centreString(REPLAY, g, fm, GS_Game.HEIGHT / 2 + 50);
        g.setColor(saved);
    }

    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, (GS_Game.WIDTH - width) / 2, yPos);
    }
}
