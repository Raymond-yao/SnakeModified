package online_local.client;

import Models.Snake;
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


    // Render data for anime
    public String renderData;
    public Pair food;
    public Map.Entry specialFood;
    public Integer decay;
    public List<Pair> pairlist1 = new LinkedList<>();
    public List<Pair> pairlist2 = new LinkedList<>();
    public int number_assigned;
    public String direction;
    private GS_Client gsClient;

    public Client() {
        try {
            socket = new Socket("localhost", 12345);
            this.out = new PrintWriter(socket.getOutputStream(),true);
            gsClient = new GS_Client(this, new Dimension(780, 780));
            new Renderer().start();
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
            food = new Pair(render.getInt("FOOD_x"), render.getInt("FOOD_x"));
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
            //decay = (Integer) Integer.parseInt(render.getInt("SF_d"));
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
                    if (inword.matches("(.+);(.+)")) {
                        height = Integer.parseInt(inword.split(";")[0]);
                        width = Integer.parseInt(inword.split(";")[1]);
                        System.out.println(height + "," + width);
                        gsClient.setVisible(true);
                    } else {
                        parseData(inword);
                        System.out.println(inword);
                        gsClient.repaint();
                        System.out.println("finish paint");
                    }


                }
                System.out.println("out of the loop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Client();

    }
}
