package com.jumping.pandajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Igor on 27.02.16.
 */
public class TextOnScreenActor extends CustomActor {


    public static final float SCREEN_WEIDHT = 1080; //Gdx.graphics.getWidth();
    public static final float SCREEN_HEIGHT = 1780;//Gdx.graphics.getHeight();
    public static final float SCORING_TEXT_SIZE = 0.9f; //percent from SCREEN_WEIDHT
    public static final float INFO_TEXT_SIZE = 0.46f; //percent from SCREEN_WEIDHT

    private int speedForDisplay = 100;
    private final int speedStepForDisplay = 10;
    private int jumpForDisplay = 100;
    private final int jumpStepForDisplay = 5;

    BitmapFont scoringFont = new BitmapFont();
    BitmapFont infoFont = new BitmapFont();

    private float scoringScale = SCORING_TEXT_SIZE * SCREEN_WEIDHT / 100;
    private float infoScale = INFO_TEXT_SIZE * SCREEN_WEIDHT / 100;

    private final MainGameScreen screen;

    public TextOnScreenActor(TextureRegion textureRegion, MainGameScreen screen) {
        super(textureRegion);
        this.screen = screen;
        scoringFont.setColor(Color.WHITE);
        infoFont.setColor(Color.WHITE);

        scoringFont.getData().setScale(scoringScale);
        infoFont.getData().setScale(infoScale);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {


        if (screen.isGameOver()) {
            if (!screen.isGameWin()) {

                batch.draw(getTextureRegion(), SCREEN_WEIDHT / 2 - (0.42f * SCREEN_WEIDHT / 2),
                        SCREEN_HEIGHT / 2 - (0.2f * SCREEN_HEIGHT / 2));
                infoFont.draw(batch, "Score" + " " + String.valueOf(screen.getProgressCounter()), 35f * SCREEN_WEIDHT / 100, 30f * SCREEN_HEIGHT / 100);


                if (screen.getNumberClickOnAd() < screen.getAmountOfCountinueWithAdvertising() && !screen.getMyRequestHandler().getFailedToLoadInterstitialAd()) {
                    infoFont.draw(batch, "To continue from HERE", 15f * SCREEN_WEIDHT / 100, 20f * SCREEN_HEIGHT / 100);
                    infoFont.draw(batch, "click on advertising", 22f * SCREEN_WEIDHT / 100, 15f * SCREEN_HEIGHT / 100);

                }
            } else {
                //игра выйграна
                scoringFont.draw(batch, "YOU WIN!", 0.22f * SCREEN_WEIDHT, 0.6f * SCREEN_HEIGHT);


            }

        } else {


            //отступы указаны в процентах
            scoringFont.draw(batch, String.valueOf(screen.getProgressCounter()), (9.2f * SCREEN_WEIDHT) / 100, 10.4f * SCREEN_HEIGHT / 100);


            infoFont.draw(batch, "Jump" + " " + jumpForDisplay, SCREEN_WEIDHT - (32.4f * SCREEN_WEIDHT / 100), SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));
            infoFont.draw(batch, "Speed" + " " + speedForDisplay, 4.6f * SCREEN_WEIDHT / 100, SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));

//            try {
//
//            }
//            catch (Exception e) {
//                infoFont.draw(batch, "Jump" + " ", SCREEN_WEIDHT - (32.4f * SCREEN_WEIDHT / 100), SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));
//                infoFont.draw(batch, "Speed" + " ", 4.6f * SCREEN_WEIDHT / 100, SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));
//            }
        }
    }

    public void decreaseDisplaySpeed() {
        if (speedForDisplay - speedStepForDisplay > 0) {
            speedForDisplay -= speedStepForDisplay;

        }
    }

    public void increaseDisplaySpeed() {
        speedForDisplay += speedStepForDisplay;
    }

    public void decreaseDisplayJump() {
        if (jumpForDisplay - jumpStepForDisplay > 0) {
            jumpForDisplay -= jumpStepForDisplay;
        }
    }

    public void increaseDisplayJump() {
        jumpForDisplay += jumpStepForDisplay;

    }

    public void setSpeedForDisplay(int speedForDisplay) {
        this.speedForDisplay = speedForDisplay;
    }

    public void setJumpForDisplay(int jumpForDisplay) {
        this.jumpForDisplay = jumpForDisplay;
    }
}
