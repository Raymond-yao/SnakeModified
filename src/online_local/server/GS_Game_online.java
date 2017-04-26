package online_local.server;
import Models.Snake;
import Models.Food;
import Models.SpecialFood;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

/**
 * Created by ray on 2017/1/18.
 */


/*
* A meassage sent by players is in the form "up" , "down", "right" ,"left"*
* A message sent by server is in the form "(\d,\d;)+&(\d,\d;)+"*/
public class GS_Game_online {

    // Game setup data
    public static final int COLUMNS = 26;
    public static final int ROWS = 26;
    public static final int HEIGHT = COLUMNS * Snake.BODY_SIZE;
    public static final int WIDTH = ROWS * Snake.BODY_SIZE;
    private boolean isOver;
    private boolean hasFood;
    private boolean hasSpecialFood = false;

    // An array storing the players.
    private Socket[] players;


    private Snake snake1;
    private Snake snake2;
    private String snake1_facing;
    private String snake2_facing;
    private Snake[] snakes; // just for convience of loop operation;
    private Food food;
    private SpecialFood specialFood;


    public GS_Game_online(Socket[] players) {
        this.players = players;
        setup();
        new PlayerListener(players[0], 1).start();
        new PlayerListener(players[1], 2).start();
        update();
        gameOver();
    }

    public Snake getSnake1() {
        return snake1;
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
        snake1 = new Snake(6, 5);
        snake1_facing = snake1.getFacing();
        snake2 = new Snake(6, 15);
        snake2_facing = snake2.getFacing();
        snakes = new Snake[2];
        snakes[0] = snake1;
        snakes[1] = snake2;
        hasFood = false;
        createFood();
        isOver = false;
        System.out.print("setup finish");
    }

    private void update() {

        JSONObject renderData;

        try {
            // Provide Game setup data, and gives start signal
            broadcast(Integer.toString(HEIGHT) + ";" + Integer.toString(WIDTH));
            // broadcast("/Game Start!");

            // Main game logic
            while (!isOver) {

                renderData = writeJsonFile();
                broadcast(renderData.toString());

                snake1.setFacing(snake1_facing);
                snake2.setFacing(snake2_facing);
                snake1.move();
                snake2.move();
                checkCollision();
                eatAndGrow();
                createFood();
                decaySpecialFood();
                sleep(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String msg) {

        PrintWriter nextinfo;

        for (Socket s : players) {
            nextinfo = sendPlayerInfo(s);
            if (nextinfo == null)
                return;
            nextinfo.println(msg);
        }
    }

    // Inform all the players if the game is over
    private void gameOver() {
    }

    private JSONObject writeJsonFile() {
        JSONObject render = new JSONObject();
        JSONArray snake1 = new JSONArray();
        JSONArray snake2 = new JSONArray();

        try {

            for (Snake.Body body : this.snake1.getBodyList()) {
                JSONObject cell = new JSONObject();
                cell.put("coor_x", body.x);
                cell.put("coor_y", body.y);
                snake1.put(cell);
            }
            for (Snake.Body body : this.snake2.getBodyList()) {
                JSONObject cell = new JSONObject();
                cell.put("coor_x", body.x);
                cell.put("coor_y", body.y);
                snake2.put(cell);
            }

            render.put("Snake1", snake1);
            render.put("Snake1Dir", this.snake1.getFacing());
            render.put("Snake2", snake2);
            render.put("Snake2Dir", this.snake2.getFacing());
            render.put("FOOD_x", food.getX());
            render.put("FOOD_y", food.getY());
            //render.put("SF_x", specialFood.getX());
            //render.put("SF_y", specialFood.getY());
            //render.put("SF_d", specialFood.decay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return render;
    }


    private PrintWriter sendPlayerInfo(Socket s) {

        OutputStreamWriter out;
        BufferedWriter bfout;
        PrintWriter pw;

        try {
            out = new OutputStreamWriter(s.getOutputStream());
            bfout = new BufferedWriter(out);
            pw = new PrintWriter(bfout, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return pw;
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

    private void createFood() {
        if (!hasFood) {
            food = new Food();
            while (checkEaten(snake1, food) || checkEaten(snake2, food)) {
                food = new Food();
            }
            hasFood = true;
        }
        if (!hasSpecialFood) {
            int probability = ThreadLocalRandom.current().nextInt(0, 100 + 1);
            if (probability == 2) {
                specialFood = new SpecialFood();
                while (checkEaten(snake1, food) || checkEaten(snake2, food))
                    specialFood = new SpecialFood();
                hasSpecialFood = true;
            }
        }
    }

    private void checkCollision() {


        for (int i = 0; i < 2; i++) {
            Snake.Body head = snakes[i].getHead();
            if (head.x >= COLUMNS+1  || head.y >= ROWS +1 || head.x <= -1 || head.y <= -1) {
                isOver = true;
                return;
            }
            for (Snake.Body body : snake1.getBodyList()) {
                if (head.equals(body) && head != body) {
                    isOver = true;
                    break;
                }
            }
            for (Snake.Body body : snake2.getBodyList()) {
                if (head.equals(body) && head != body) {
                    isOver = true;
                    break;
                }
            }
        }
    }

    private boolean checkEaten(Snake snake, Food food) {
        return (food.eaten(snake.getHead().x, snake.getHead().y));
    }

    private void eatAndGrow() {
        for (int i = 0; i < 2; i++) {
            if (checkEaten(snakes[i], food)) {
                hasFood = false;
                snakes[i].grow();
            }
            if (specialFood != null && checkEaten(snakes[i], specialFood)) {
                hasSpecialFood = false;
                if (specialFood.decay > 3) {
                    snakes[i].grow();
                    snakes[i].grow();
                }
                specialFood = null;
            }
        }
    }

    private class PlayerListener extends Thread {

        private BufferedReader playerinfo;
        Socket player;
        String dir;
        int player_num;

        public PlayerListener(Socket socket, int num) {
            super();
            player = socket;
            player_num = num;
        }

        private BufferedReader getPlayerInfo(Socket s) {

            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return in;
        }

        @Override
        public void run() {
            playerinfo = getPlayerInfo(player);
            if (playerinfo == null)
                return;
            try {
                while ((dir = playerinfo.readLine()) != null) {
                    switch (player_num) {
                        case 1:
                            snake1_facing = dir;
                            break;
                        case 2:
                            snake2_facing = dir;
                            break;
                    }
                    System.out.println("set new directions");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
