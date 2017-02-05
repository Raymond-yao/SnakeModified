package offline;

/**
 * Created by ray on 2017/1/18.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.Timer;


/**
 * Created by ray on 2016/9/18.
 */
public class GreedySnakeSinglePlayer extends JFrame {
    private static final int INTERVAL = 150;
    private GS_GameSinglePlayer game;
    private GamePanelSinglePlayer gp;


    public GreedySnakeSinglePlayer() {
        super("Greedy Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        game = new GS_GameSinglePlayer();
        gp = new GamePanelSinglePlayer(game);
        add(gp);
        addKeyListener(new KeyHandler());
        pack();
        centreOnScreen();
        setVisible(true);
        addTimer();
    }

    private void addTimer() {
        Timer t = new Timer(INTERVAL, null);
        t.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!game.isOver()) {
                    game.update();
                }
                gp.repaint();
            }
        });

        t.start();
    }

    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            game.keyPressed(e.getKeyCode());
        }
    }

    // Play the game
    public static void main(String[] args) {
        new GreedySnakeSinglePlayer();
    }
}

