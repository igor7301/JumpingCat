package com.jumping.pandajump;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Igor on 26.02.16.
 */
public class CustomActor extends Actor {

    private boolean collisionWithCharacter;
    private Circle circle = new Circle();
    private Rectangle rectangle = new Rectangle();
    private final TextureRegion textureRegion;

    public CustomActor(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }


       @Override
    public void draw(Batch batch, float parentAlpha) {
           if(!getCollisionWithCharacter()) {
               batch.draw(textureRegion, getX(), getY());
           }
    }

    public Circle getCircleBounds() {
        this.circle.setX(getX() + getTextureRegion().getRegionWidth() / 2);
        this.circle.setY(getY() + getTextureRegion().getRegionHeight() / 2);
        this.circle.setRadius(getTextureRegion().getRegionHeight() / 2);
        return circle;
    }


    public Rectangle getRectangleBounds() {
        this.rectangle.x = getX();
        this.rectangle.y = getY();
        this.rectangle.width = textureRegion.getRegionWidth();
        this.rectangle.height = textureRegion.getRegionHeight();
        return rectangle;
    }

    public boolean getCollisionWithCharacter() {
        return collisionWithCharacter;
    }

    public CustomActor setCollisionWithCharacter(boolean collisionWithCharacter) {
        this.collisionWithCharacter = collisionWithCharacter;
        return this;
    }



    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

}
