package com.jumping.jumpingcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Poison {

    private final TextureRegion textureRegion;
    private float x;
    private float y;
    boolean collisionWithCharacter;
    Circle circle = new Circle();


    public Poison(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public static int getRandomY(int screenHeight) {
        int minY = screenHeight / 2;
        int dispersionY = screenHeight / 4;
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
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public float getHeight() {
        return textureRegion.getRegionHeight();
    }
}
