package offline;

import Models.Food;
import Models.Snake;
import Models.SpecialFood;
import offline.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/1/18.
 */
public class GamePanelDoublePlayer extends JPanel {

    private static final String OVER = "Game Over!";
    private static final String REPLAY = "R to replay";

    private GS_GameDoublePlayer game;
    private Color foodColor;


    public GamePanelDoublePlayer(GS_GameDoublePlayer game) {
        setPreferredSize(new Dimension(GS_GameSinglePlayer.WIDTH, GS_GameSinglePlayer.HEIGHT));
        setBackground(Color.GRAY);
        this.game = game;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);

    }

    private void drawGame(Graphics g) {
        drawSnakes(g);
        drawFood(g);
    }

    private void drawSnakes(Graphics g) {
        Snake[] snakes = game.getSnakes();
        drawSnake(g, snakes[0]);
        drawSnake(g, snakes[1]);
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

    private void drawSnake(Graphics g, Snake s) {
        Snake snake = s;
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
        centreString(OVER, g, fm, GS_GameSinglePlayer.HEIGHT / 2);
        centreString(REPLAY, g, fm, GS_GameSinglePlayer.HEIGHT / 2 + 50);
        g.setColor(saved);
    }

    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
        int width = fm.stringWidth(str);
        g.drawString(str, (GS_GameSinglePlayer.WIDTH - width) / 2, yPos);
    }


}
