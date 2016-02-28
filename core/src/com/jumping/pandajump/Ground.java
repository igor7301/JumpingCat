package com.jumping.pandajump;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Ground  extends CustomActor {

    boolean roofCompleted;


    public Ground(TextureRegion textureRegion) {
        super(textureRegion);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw (getTextureRegion(), getX(), getY());
    }


    public static int getRandomY(int screenHeight) {
        //переводим проценты в пиксели
        //// TODO: 15.02.16  Update needed
        int textureHeight = (int) (screenHeight * 0.6756f);
        int epsilon = (int) (0.15f  * screenHeight);
        int minY =  - textureHeight + epsilon ;
        int dispersionY = textureHeight - epsilon;
        int rand = new Random(System.currentTimeMillis()).nextInt(dispersionY);


        return minY + rand;
    }





    public boolean getGroundCompleted() {
        return roofCompleted;
    }

    public void setGroundCompleted(boolean roofCompleted) {
        this.roofCompleted = roofCompleted;
    }


}
