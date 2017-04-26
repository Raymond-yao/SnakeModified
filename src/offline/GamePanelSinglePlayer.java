package offline;

import java.awt.*;

/**
 * Created by ray on 2017/1/18.
 */
public class GamePanelSinglePlayer extends GamePanel {


    public GamePanelSinglePlayer(GS_Game game) {
        super(game);
        setPreferredSize(new Dimension(GS_Game.WIDTH, GS_Game.HEIGHT));
        setBackground(Color.GRAY);

    }


}
