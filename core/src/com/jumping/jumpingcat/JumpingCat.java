package com.jumping.jumpingcat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JumpingCat extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] bird = new Texture[2];
    Texture bottomTube;
    private int birdState;
    private float birdY;
    boolean gameIsRunning;
    float descentVelocity;
    private float jumpSize = 30f;
    private Texture topTube;
    private int gap = 300;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        bird[0] = new Texture("bird.png");
        bird[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;
        bottomTube = new Texture("bottomtube.png");
        topTube = new Texture("toptube.png");

    }

    @Override
    public void render() {

        if (gameIsRunning) {

            //обработка прыжка
            if (Gdx.input.justTouched()) {
                descentVelocity = -jumpSize;

            }

            //обработка падения не ниже нижней границы экрана
            if (birdY > 0 || descentVelocity < 0) {
                descentVelocity++;
                birdY -= descentVelocity;
            }


            if (birdState == 0) {
                birdState = 1;
            } else {
                birdState = 0;
            }


        } else {
            if (Gdx.input.justTouched()) {
                gameIsRunning = true;

            }
        }


        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(bottomTube,
                Gdx.graphics.getWidth() / 2 - bottomTube.getWidth() / 2,
                Gdx.graphics.getHeight()/2 - bottomTube.getHeight() - gap/2);
        batch.draw(topTube,
                Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2,
                Gdx.graphics.getHeight()/2 + gap/2);
        batch.draw(bird[birdState],
                Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2,
                birdY,
                bird[birdState].getWidth(), bird[birdState].getHeight());


        batch.end();

    }
}
