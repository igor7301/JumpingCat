package com.jumping.pandajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Igor on 25.02.16.
 */
public class Hero extends CustomActor {

    Animation animation;
    short stateTime;



    public Hero(TextureRegion textureRegion) {
        super(textureRegion);

    }



    @Override
    public void draw(Batch batch, float parentAlpha) {

        stateTime ++;
        if (stateTime >= Short.MAX_VALUE ) {
            stateTime = 0;
        }
        if (animation != null) {
            batch.draw(animation.getKeyFrame(stateTime, true), getX(), getY());
            Gdx.app.log("App", "" + stateTime);
        }
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }







    public TextureRegion getTextureRegion() {
        if (animation!= null) {

        return animation.getKeyFrame(stateTime, true);
        }
        return null;
    }
}
