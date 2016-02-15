package com.jumping.jumpingcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Ground extends Texture {

    private float x;
    private float y;
    Rectangle rectangle = new Rectangle();
    boolean roofCompleted;


    public Ground(String internalPath) {
        super(internalPath);
    }


    public float getX() {
        return x;
    }

    public Ground setX(float x) {
        this.x = x;
        return this;
    }

    public void decreaseX(float x) {
        this.x -= x;
    }

    public float getY() {
        return y;
    }

    public Ground setY(int y) {
        this.y = y;
        return this;
    }

    public static int getRandomY(int screenHeight) {
        //переводим проценты в пиксели
        //// TODO: 15.02.16  Update needed
        int textureHeight = (int) (screenHeight * 0.6756f);
        int epsilon = (int) (0.1f  * screenHeight);
        int minY =  - textureHeight + epsilon ;
        int dispersionY = textureHeight - epsilon;
        int rand = new Random(System.currentTimeMillis()).nextInt(dispersionY);


        return minY + rand;
    }

    public Rectangle getRectangleBounds() {
        return rectangle;
    }



    public boolean getGroundCompleted() {
        return roofCompleted;
    }

    public void setGroundCompleted(boolean roofCompleted) {
        this.roofCompleted = roofCompleted;
    }

    public void setRectangleBounds(float x, float y, float width, float height) {
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;

    }
}
