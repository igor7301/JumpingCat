package com.jumping.jumpingcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Food extends Texture {

    private int x;
    private int y;
    boolean collisionWithCharacter;
    Circle circle = new Circle();


    public Food(String internalPath) {
        super(internalPath);
    }

    public static int getRandomY() {
        int minY = Gdx.graphics.getHeight() / 2;
        int dispersionY = Gdx.graphics.getHeight() / 4;
        return minY + new Random(System.currentTimeMillis()).nextInt(dispersionY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Food setX(int x) {
        this.x = x;
        return this;
    }

    public void decreaseX(int x) {
        this.x -= x;
    }

    public Food setY(int y) {
        this.y = y;
        return this;
    }

    public boolean getCollisionWithCharacter() {
        return collisionWithCharacter;
    }

    public Food setCollisionWithCharacter(boolean collisionWithCharacter) {
        this.collisionWithCharacter = collisionWithCharacter;
        return this;
    }

    public Circle getCircleBounds() {
        return circle;
    }

    public void setCircleBounds(int x, int y, int radius) {
        this.circle.setX(x);
        this.circle.setY(y);
        this.circle.setRadius(radius);
    }
}
