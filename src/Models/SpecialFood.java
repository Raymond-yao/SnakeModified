package Models;

import java.awt.*;

/**
 * Created by 62442 on 2017-01-18.
 */
public class SpecialFood extends Food {

    public int decay;
    public Color decayColor;
    private int red;
    private int green;
    private int blue;

    public SpecialFood() {
        super();
        decay = 15;
        red = 242;
        green = 36;
        blue = 241;
        decayColor = new Color(red, green, blue);
    }

    public SpecialFood(int x, int y, int decay){
        this.x = x;
        this.y = y;
        red = 242;
        green = 36;
        blue = 241;
        decayColor = new Color(red, green + (decay * 10), blue - (decay * 10));
    }

    public void decay() {
        decay--;
        green = green + 10;
        blue = blue - 10;
        decayColor = new Color(red, green, blue);
    }


}
