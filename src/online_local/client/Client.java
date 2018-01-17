package online_local.client;

import Models.Snake;
import Models.SpecialFood;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ray on 2017/1/29.
 */
public class Client {

    private Socket socket;
    private Snake snake1;
    private Snake snake2;
    PrintWriter out;
    BufferedReader in;
    private int height;
    private int width;
    public boolean hasSpecialFood;

    // Render data for anime
    public Pair food;
    public SpecialFood specialFood;
    public Integer decay;
    public List<Pair> pairlist1 = new LinkedList<>();
    public List<Pair> pairlist2 = new LinkedList<>();
    public int number_assigned;
    public String direction;
    private GS_Client gsClient;

    public Client(String ip,int port) {
        try {
            socket = new Socket(ip, port);
            this.out = new PrintWriter(socket.getOutputStream(),true);
            gsClient = new GS_Client(this, new Dimension(780, 780));
            hasSpecialFood = false;
            new Renderer().start();
            System.out.println("done setup");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDir(String msg) throws IOException {
        out.println(msg);
    }

    private void parseData(String data) {
        JSONObject render;
        JSONArray snake1;
        JSONArray snake2;


        try {
            render = new JSONObject(data);
            snake1 = render.getJSONArray("Snake1");
            snake2 = render.getJSONArray("Snake2");
            food = new Pair(render.getInt("FOOD_x"), render.getInt("FOOD_y"));
            hasSpecialFood = render.getBoolean("hasSpecial");
            if (hasSpecialFood)
                specialFood = new SpecialFood(render.getInt("SF_x"),render.getInt("SF_y"),render.getInt("SF_d"));
            switch (number_assigned) {
                case 1:
                    direction = render.getString("Snake1Dir");
                    System.out.println(direction);
                    break;
                case 2:
                    direction = render.getString("Snake2Dir");
                    System.out.println(direction);
                    break;
            }
            pairlist1.clear();
            pairlist2.clear();
            for (int i = 0; i < snake1.length(); i++) {
                JSONObject cell = snake1.getJSONObject(i);
                Pair pair = new Pair(cell.getInt("coor_x"), cell.getInt("coor_y"));
                pairlist1.add(pair);
            }

            for (int i = 0; i < snake2.length(); i++) {
                JSONObject cell = snake2.getJSONObject(i);
                Pair pair = new Pair(cell.getInt("coor_x"), cell.getInt("coor_y"));
                pairlist2.add(pair);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class Renderer extends Thread {

        public Renderer() {
            super();
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inword = null;

                while ((inword = in.readLine()) != null) {
                    if (inword.matches("/(.+)"))
                        number_assigned = Integer.parseInt(inword.split(";")[1]);
                    else if (inword.matches("(.+);(.+)")) {
                        height = Integer.parseInt(inword.split(";")[0]);
                        width = Integer.parseInt(inword.split(";")[1]);
                        gsClient.setVisible(true);
                    } else if (inword.equals("Game Over!")) {
                        System.out.println(inword);
                        in.close();
                        out.close();
                        socket.close();
                        gsClient.setVisible(false);
                        gsClient.dispose();
                        return;
                    } else {
                        parseData(inword);
                        gsClient.repaint();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Client("localhost",12345);

    }
}
