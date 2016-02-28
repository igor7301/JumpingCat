package com.jumping.pandajump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created by Igor on 20.02.16.
 */
public class MyMainGame extends Game{

    private IActivityRequestHandler iActivityRequestHandler;
    private SpriteBatch spriteBatch;
    private MainGameScreen mainGameScreen;
    private StartScreen startScreen;


    public MyMainGame(IActivityRequestHandler iActivityRequestHandler) {
        this.iActivityRequestHandler = iActivityRequestHandler;
    }



    @Override
    public void create() {

        spriteBatch = new SpriteBatch();
        mainGameScreen = new MainGameScreen(this);
        startScreen = new StartScreen(this);
        this.setScreen(mainGameScreen);



    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
    }

    public IActivityRequestHandler getRequestHandler() {
        return iActivityRequestHandler;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public MainGameScreen getMainGameScreen() {
        return mainGameScreen;
    }

    public StartScreen getStartScreen() {
        return startScreen;
    }


}
