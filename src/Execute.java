import offline.GS_GameDoublePlayer;
import offline.GS_GameSinglePlayer;
import offline.GreedySnakeDoublePlayer;
import offline.GreedySnakeSinglePlayer;
import online_local.client.Client;
import online_local.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ray on 2017/2/4.
 */
public class Execute {

    public static void main(String [] args){
        BufferedReader userword = new BufferedReader(new InputStreamReader(System.in));
        String word = null;
        try {
            while ((word = userword.readLine()) != null) {
                switch (word){
                    case "single":
                        new GreedySnakeSinglePlayer();
                        break;
                    case "double":
                        new GreedySnakeDoublePlayer();
                        break;
                    case "server":
                        new Server(12345);
                        break;
                    case "client":
                        new Client("localhost",12345);
                        break;
                    case "exit":
                        return;
                    default:
                        return;
                }
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
