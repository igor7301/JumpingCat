package com.jumping.jumpingcat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Health extends Texture {
    private float x;
    private float y;
    boolean collisionWithCharacter;
    Circle circle = new Circle();

    public Health(String internalPath) {
        super(internalPath);
    }

    public float getX() {
        return x;
    }

    public Health setX(float x) {
        this.x = x;
        return this;
    }

    public void decreaseX(float x) {
        this.x -= x;
    }

    public float getY() {
        return y;
    }

    public Health setY(float y) {
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

    public void setCircleBounds(float x, float y, float radius) {
        this.circle.setX(x);
        this.circle.setY(y);
        this.circle.setRadius(radius);
    }
}
