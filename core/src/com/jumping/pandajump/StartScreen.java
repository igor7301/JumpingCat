package com.jumping.pandajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Igor on 23.02.16.
 */
public class StartScreen implements Screen {
    public static final float INFO_TEXT_SIZE = 0.46f; //percent from SCREEN_WEIDHT
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WEIDHT;
    private final TextureAtlas textureAtlas;
    private final TextureRegion background;
    private final StretchViewport viewport;
    private MyMainGame game;
    private SpriteBatch batch;
    private BitmapFont headingFont;
    private float fontScale;
    private Stage stage;



    public StartScreen(MyMainGame game) {
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        SCREEN_WEIDHT = Gdx.graphics.getWidth();

        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage = new Stage(viewport, game.getSpriteBatch());

        this.game = game;

        textureAtlas = new TextureAtlas(Gdx.files.internal("atlas.pack"));
        background = textureAtlas.findRegion("bg");
        batch = game.getSpriteBatch();
        headingFont = new BitmapFont();


        stage.act(Gdx.graphics.getDeltaTime());


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        fontScale = INFO_TEXT_SIZE * SCREEN_WEIDHT / 100;
        headingFont.setColor(Color.WHITE);

        headingFont.getData().setScale(fontScale);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, SCREEN_WEIDHT, SCREEN_HEIGHT);
        headingFont.draw(stage.getBatch(), "Play PANDA JUMP", 15f * SCREEN_WEIDHT / 100, 20f * SCREEN_HEIGHT / 100);
        stage.getBatch().end();

        stage.draw();


        if (Gdx.input.justTouched()) {
            game.setScreen(game.getMainGameScreen());
        }



    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
