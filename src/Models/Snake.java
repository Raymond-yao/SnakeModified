package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 2017/1/18.
 */
public class Snake {

    private String facing;
    private List<Body> bodyList;
    private Body head;
    private boolean grow;
    private int size;
    public static final int BODY_SIZE = 30;

    public Snake(int posx,int posy) {
        facing = "right";
        grow = false;
        bodyList = new ArrayList<Body>();
        for (int i = 0; i < 3; i++) {
            Body body = new Body(posx - i, posy);
            bodyList.add(body);
        }
        head = bodyList.get(0);
        size = bodyList.size();
    }

    public Snake() {
        facing = "right";
        grow = false;
        bodyList = new ArrayList<Body>();
        for (int i = 0; i < 3; i++) {
            Body body = new Body(4 - i, 10);
            bodyList.add(body);
        }
        head = bodyList.get(0);
        size = bodyList.size();
    }

    public void grow() {
        grow = true;
    }

    public Body getHead() {
        return head;
    }

    public List<Body> getBodyList() {
        return bodyList;
    }

    public int getBodyX(int i) {
        return bodyList.get(i).x;
    }

    public int getBodyY(int i) {
        return bodyList.get(i).y;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        switch (facing) {
            case "up":
                if (!this.facing.equals("up") && !this.facing.equals("down"))
                    this.facing = facing;
                break;
            case "down":
                if (!this.facing.equals("up") && !this.facing.equals("down"))
                    this.facing = facing;
                break;
            case "right":
                if (!this.facing.equals("right")  && !this.facing.equals("left"))
                    this.facing = facing;
                break;
            case "left":
                if (!this.facing.equals("right")  && !this.facing.equals("left"))
                    this.facing = facing;
                break;
        }
    }

    public void move() {
        switch (facing) {
            case "right":
                moveOneBlock(1, 0);
                break;
            case "left":
                moveOneBlock(-1, 0);
                break;
            case "up":
                moveOneBlock(0, -1);
                break;
            case "down":
                moveOneBlock(0, 1);
                break;
        }
    }

    public void moveOneBlock(int dx, int dy) {
        bodyList.add(0, new Body(head.x + dx, head.y + dy));
        head = bodyList.get(0);
        if (!grow) {
            bodyList.remove(bodyList.size() - 1);
            return;
        }
        size = bodyList.size();
        grow = false;
    }


    public class Body {

        public int x;
        public int y;

        public Body(int x, int y) {
            this.x = x;
            this.y = y;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Body body = (Body) o;

            if (x != body.x) return false;
            return y == body.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
