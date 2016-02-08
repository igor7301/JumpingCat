package com.jumping.jumpingcat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Health extends Texture {
    private int x;
    private int y;
    boolean collisionWithCharacter;
    Circle circle = new Circle();

    public Health(String internalPath) {
        super(internalPath);
    }

    public int getX() {
        return x;
    }

    public Health setX(int x) {
        this.x = x;
        return this;
    }

    public void decreaseX(int x) {
        this.x -= x;
    }

    public int getY() {
        return y;
    }

    public Health setY(int y) {
        this.y = y;
        return this;
    }


    public boolean getCollisionWithCharacter() {
        return collisionWithCharacter;
    }

    public Health setCollisionWithCharacter(boolean collisionWithCharacter) {
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
