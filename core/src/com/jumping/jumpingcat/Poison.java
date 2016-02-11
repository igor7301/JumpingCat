package com.jumping.jumpingcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Poison extends Texture {

    private float x;
    private float y;
    boolean collisionWithCharacter;
    Circle circle = new Circle();


    public Poison(String internalPath) {
        super(internalPath);
    }

    public static int getRandomY() {
        int minY = Gdx.graphics.getHeight() / 2;
        int dispersionY = Gdx.graphics.getHeight() / 4;
        return minY + new Random(System.currentTimeMillis()).nextInt(dispersionY);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Poison setX(float x) {
        this.x = x;
        return this;
    }

    public void decreaseX(float x) {
        this.x -= x;
    }

    public Poison setY(int y) {
        this.y = y;
        return this;
    }

    public boolean getCollisionWithCharacter() {
        return collisionWithCharacter;
    }

    public Poison setCollisionWithCharacter(boolean collisionWithCharacter) {
        this.collisionWithCharacter = collisionWithCharacter;
        return this;
    }

    public Circle getCircleBounds() {
        return circle;
    }

    public void setCircleBounds(float x, float y, float radius) {
        this.circle.setX(x);
        this.circle.setY(y);
        this.circle.setRadius(radius);
    }
}