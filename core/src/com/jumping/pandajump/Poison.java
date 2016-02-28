package com.jumping.pandajump;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by ikomarov on 08.02.2016.
 */
public class Poison extends CustomActor {




    public Poison(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public static int getRandomY(int screenHeight) {
        int minY = screenHeight / 2;
        int dispersionY = screenHeight / 4;
        return minY + new Random(System.currentTimeMillis()).nextInt(dispersionY);
    }




}
