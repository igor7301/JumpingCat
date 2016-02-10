package com.jumping.jumpingcat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Ground extends Texture {

    private int x;
    private int y;
    Rectangle rectangle = new Rectangle();
    boolean roofCompleted;


    public Ground(String internalPath) {
        super(internalPath);
    }


    public int getX() {
        return x;
    }

    public Ground setX(int x) {
        this.x = x;
        return this;
    }

    public void decreaseX(int x) {
        this.x -= x;
    }

    public int getY() {
        return y;
    }

    public Ground setY(int y) {
        this.y = y;
        return this;
    }

    public static int getRandomY(Ground ground) {
        int minY =  - ground.getHeight() + 50 ;
        int dispersionY = ground.getHeight() / 2;
        return minY + new Random(System.currentTimeMillis()).nextInt(dispersionY);
    }

    public Rectangle getRectangleBounds() {
        return rectangle;
    }



    public boolean getRoofCompleted() {
        return roofCompleted;
    }

    public void setRoofCompleted(boolean roofCompleted) {
        this.roofCompleted = roofCompleted;
    }

    public void setRectangleBounds(int x, int y, int width, int height) {
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;

    }
}
