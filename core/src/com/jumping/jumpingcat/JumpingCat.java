package com.jumping.jumpingcat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class JumpingCat extends ApplicationAdapter {
    public static final float FULL_JUMP_SIZE = 40f;
    public static final int DELAY_BETWEEN_CHARACTER_ACTIONS = 5;
    public static final int NUMBER_OF_CHARACTERS_STATES = 8;
    SpriteBatch batch;
    Texture background;
    Texture gameOverTexture;
    Health health;
    Texture[] bird = new Texture[NUMBER_OF_CHARACTERS_STATES];
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    private int birdState;
    private float birdY;
    private float birdX;
    BitmapFont scoringFont;
    BitmapFont weightFont;

    private final int numberOfRoofs = 4;
    private final int numberOfFood = 4;

    Food[] food = new Food[numberOfFood];
    Ground[] ground = new Ground[numberOfFood];
    private final int roofOffsetVelocityX = 5;

    boolean gameIsRunning;
    boolean gameOver;
    float descentVelocity;
    private float jumpSize;
    private final int distanceBetweenRoof = 1200;
    private final int distanceBetweenFood = 900;
    private final int distanceBetweenHealth = 2000;
    private final int jumpDecrease = 2; //снижение способности прыгать когда ешь еду
    private int progressCounter;
    private int currentCharacterDelay;


    @Override
    public void create() {
        gameIsRunning = false;
        gameOver = false;
        descentVelocity = 0;
        jumpSize = FULL_JUMP_SIZE;
        progressCounter = 0;

        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        scoringFont = new BitmapFont();
        weightFont = new BitmapFont();

        scoringFont.setColor(Color.WHITE);
        weightFont.setColor(Color.WHITE);

        scoringFont.getData().setScale(10);
        weightFont.getData().setScale(5);



        batch = new SpriteBatch();
        background = new Texture("bg.png");



        for (int i = 0; i < numberOfFood; i++) {
            food[i] = new Food("food.png");
            food[i].setX(i * distanceBetweenFood).setY(Food.getRandomY());
        }

        health = new Health("health.png");
        health.setX(distanceBetweenHealth);
        health.setY(Gdx.graphics.getHeight() / 2);


        bird[0] = new Texture("dog1.png");
        bird[1] = new Texture("dog2.png");
        bird[2] = new Texture("dog3.png");
        bird[3] = new Texture("dog4.png");
        bird[4] = new Texture("dog5.png");
        bird[5] = new Texture("dog6.png");
        bird[6] = new Texture("dog7.png");
        bird[7] = new Texture("dog8.png");
        birdY = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;
        birdX = Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2;


        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i] = new Ground("ground.png");
            ground[i].setX(i * distanceBetweenRoof);
            ground[i].setY(Ground.getRandomY(ground[i]));
            if ( i == 0 ) {
                ground[i].setY((int) (birdY - ground[i].getHeight()));
            }

        }
        gameOverTexture = new Texture("gameover.png");

    }


    @Override
    public void render() {

        if (gameIsRunning) {

//            //прыгать всегда
//            if (Gdx.input.justTouched()) {
//                descentVelocity = -jumpSize;
//                birdY -= descentVelocity;
//
//            }

            //если птица выше нижнего экрана
            if (birdY > 0 || descentVelocity < 0) {

                //чтобы не велезать за верхнюю часть экрана
                if (birdY + bird[birdState].getHeight() >= Gdx.graphics.getHeight()) {
                    descentVelocity = 0;
                }

                //птица падает
                descentVelocity++;
                birdY -= descentVelocity;

                for (int i = 0; i < numberOfRoofs; i++) {

                    float roofTopY = ground[i].getRectangleBounds().y + ground[i].getRectangleBounds().height;
                    //если птица касается крыши сверху
                    if (Intersector.overlaps(birdCircle, ground[i].getRectangleBounds())
                            && birdCircle.y >= roofTopY && birdCircle.x > ground[i].getRectangleBounds().x) {

                        //то она остается на крыше
                        descentVelocity = 0;
                        birdY = roofTopY;

                        //прыгать можно только если птица стоит на крыше
                        if (Gdx.input.justTouched()) {
                            descentVelocity = -jumpSize;
                            birdY -= descentVelocity;

                        }

                        if (!ground[i].getRoofCompleted()){
                            ground[i].setRoofCompleted(true);
                            progressCounter++;
                        }




                        break;
                    }
                }
            } else {
                gameIsRunning = false;
                gameOver = true;
            }




            //меняем состояние птицы чтобы крыльями махала
            if (currentCharacterDelay < DELAY_BETWEEN_CHARACTER_ACTIONS) {
                currentCharacterDelay++;
            }
            else {
                currentCharacterDelay = 0;
                for (int  i = 0; i < NUMBER_OF_CHARACTERS_STATES; i++) {
                    //если это последнее возможное состояние
                    if(birdState == NUMBER_OF_CHARACTERS_STATES - 1) {
                        birdState = 0;
                    }
                    //иначе устанавливаем следующее состояние
                    else if (birdState == i) {
                        birdState = i + 1;
                        break;
                    }

                }

//                if (birdState == 0) {
//                    birdState = 1;
//                }
//                else {
//                    birdState = 0;
//                }
            }

            health.decreaseX(roofOffsetVelocityX);
            //если health ушло за границы экрана
            if (health.getX() + health.getWidth() < 0) {
                health.setX(distanceBetweenHealth);
                health.setY(Gdx.graphics.getHeight() / 2);
                health.setCollisionWithCharacter(false);
            }


            //крыши бегут непрерывно
            for (int i = 0; i < numberOfRoofs; i++) {
                ground[i].decreaseX(roofOffsetVelocityX);

                //если крыша полностью ушла за экран то вместо нее рисуем новую после последней крыши
                if (ground[i].getX() + ground[i].getWidth() < 0) {
                    ground[i].setX(ground[numberOfRoofs - 1].getX() + (i + 1) * distanceBetweenRoof);
                    ground[i].setY(Ground.getRandomY(ground[i]));
                    ground[i].setRoofCompleted(false);

                }

            }
            Gdx.app.log("Progress", "" + progressCounter);
            Gdx.app.log("JumpSize", "" + jumpSize);




            //если eда ушла за экран то вместо нее рисуем новую после последней еды
            for (int i = 0; i < numberOfFood; i++) {
                food[i].decreaseX(roofOffsetVelocityX);

                if (food[i].getX() + food[i].getHeight() < 0) {
                    food[i]
                            .setX(food[numberOfFood - 1].getX() + (i + 1) * distanceBetweenFood)
                            .setY(Food.getRandomY())
                            .setCollisionWithCharacter(false);
                }
            }


            //столкновение с едой
            for (int i = 0; i < numberOfFood; i++) {

                if (Intersector.overlaps(birdCircle, food[i].getCircleBounds())) {
                    if (!food[i].getCollisionWithCharacter()) {

                        jumpSize -= jumpDecrease;
                        food[i].setCollisionWithCharacter(true);
                    }

                }
            }

            //столкновение с health
            if (Intersector.overlaps(birdCircle, health.getCircleBounds())) {
                if (!health.getCollisionWithCharacter()) {

                    jumpSize += jumpDecrease;
                    health.setCollisionWithCharacter(true);
                }

            }



        } else {
            if (Gdx.input.justTouched()) {

                //если игра не закончена
                if (!gameOver) {
                    gameIsRunning = true;

                } else {
                    //если игра закончена начинаем игру заново
                    create();
                }

            }
        }

        birdCircle.set(birdX + bird[birdState].getWidth() / 2,
                birdY + bird[birdState].getHeight() / 2, bird[birdState].getWidth() / 2);


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(bird[birdState],
                birdX,
                birdY,
                bird[birdState].getWidth(), bird[birdState].getHeight());

        for (int i = 0; i < numberOfRoofs; i++) {
            batch.draw(ground[i], ground[i].getX(), ground[i].getY(), ground[i].getWidth(), ground[i].getHeight());
            ground[i].setRectangleBounds(ground[i].getX(), ground[i].getY(), ground[i].getWidth(), ground[i].getHeight());
        }

        for (int i = 0; i < numberOfFood; i++) {
            if (!food[i].getCollisionWithCharacter()) {
                batch.draw(food[i], food[i].getX(), food[i].getY(), food[i].getWidth(), food[i].getHeight());
            }
            food[i].setCircleBounds(food[i].getX() + food[i].getWidth() / 2,
                    food[i].getY() + food[i].getHeight() / 2,
                    food[i].getHeight() / 2);

        }
        health.setCircleBounds(health.getX() + health.getWidth() / 2, health.getY() + health.getHeight() / 2, health.getHeight() / 2);

        if (!health.getCollisionWithCharacter()) {
            batch.draw(health, health.getX(), health.getY(), health.getWidth(), health.getHeight());
        }

        scoringFont.draw(batch, String.valueOf(progressCounter), 100, 200);
        weightFont.draw(batch, String.valueOf((int)(jumpSize * 100 / FULL_JUMP_SIZE)), Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);

        if (gameOver) {
            batch.draw(gameOverTexture, Gdx.graphics.getWidth() / 2 - gameOverTexture.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2  - gameOverTexture.getHeight() / 2);
        }

        batch.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//        for (int i = 0; i < numberOfRoofs; i++) {
//            shapeRenderer.rect(ground[i].getRectangleBounds().x, ground[i].getRectangleBounds().y, ground[i].getRectangleBounds().width, ground[i].getRectangleBounds().height);
//
//        }
//        for (int i = 0; i < numberOfFood; i++) {
//            shapeRenderer.circle(food[i].getCircleBounds().x, food[i].getCircleBounds().y, food[i].getCircleBounds().radius);
//
//        }
//        shapeRenderer.end();

    }


}
