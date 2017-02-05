package online_local.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by ray on 2017/2/3.
 */
public class GS_Client extends JFrame {

    public GamePanelOnline gp;
    public Client client;

    public GS_Client(Client client, Dimension d) {
        super("Greedy Snake");
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(false);
        gp = new GamePanelOnline(client);
        gp.setPreferredSize(new Dimension(d));
        add(gp);
        addKeyListener(new KeyHandler());
        pack();
        addKeyListener(new KeyHandler());
    }

    public void repaint() {
        gp.repaint();
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            try {
                keyPressed(e.getKeyCode());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        public void keyPressed(int KeyCode) throws IOException {
            switch (KeyCode) {
                case KeyEvent.VK_UP:
                    client.sendDir("up");
                    break;
                case KeyEvent.VK_DOWN:
                    client.sendDir("down");
                    break;
                case KeyEvent.VK_RIGHT:
                    client.sendDir("right");
                    break;
                case KeyEvent.VK_LEFT:
                    client.sendDir("left");
                    break;
            }
        }
    }
}
