package online_local.client;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/2/2.
 */
public class GamePanelOnline extends JPanel  {

    private Client client;
    public static final int FOOD_SIZE = 30;
    public static final int BODY_SIZE = 30;

    public GamePanelOnline(Client client) {
        this.client = client;
        setBackground(Color.GRAY);
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
        Color foodColor = new Color(237, 237, 58);
        g.setColor(foodColor);
        g.fillOval(client.food.getX() * FOOD_SIZE, client.food.getY() * FOOD_SIZE, FOOD_SIZE, FOOD_SIZE);
    }

    private void drawSnake(Graphics g) {

        int red;
        int green;
        int blue;
        Color snakecolor;

        for (int i = 0; i < client.pairlist1.size(); i++) {
            red = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            green = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            blue = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            snakecolor = new Color(red, green, blue);
            g.setColor(snakecolor);
            g.fillRect(client.pairlist1.get(i).getX() * BODY_SIZE, client.pairlist1.get(i).getY() * BODY_SIZE, BODY_SIZE, BODY_SIZE);
        }
        for (int i = 0; i < client.pairlist2.size(); i++) {
            red = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            green = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            blue = ThreadLocalRandom.current().nextInt(0, 255 + 1);
            snakecolor = new Color(red, green, blue);
            g.setColor(snakecolor);
            g.fillRect(client.pairlist2.get(i).getX() * BODY_SIZE, client.pairlist2.get(i).getY() * BODY_SIZE, BODY_SIZE, BODY_SIZE);
        }
    }
}
