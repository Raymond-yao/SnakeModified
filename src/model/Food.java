package model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ray on 2017/1/18.
 */
public class Food {


    public static final int FOOD_SIZE = 30;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Food() {
        x = ThreadLocalRandom.current().nextInt(0, 20 + 1);
        y = ThreadLocalRandom.current().nextInt(0, 20 + 1);
    }

    public boolean eaten(int x, int y) {
        return (this.x == x && this.y == y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food food = (Food) o;

        if (x != food.x) return false;
        return y == food.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
