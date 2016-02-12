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



    public static  float SCREEN_HEIGHT;
    public static  float SCREEN_WEIDHT;
    private static float SCREEN_DIMENSION;

    public static final float SCORING_TEXT_SIZE = 0.9f; //percent from SCREEN_WEIDHT
    public static final float INFO_TEXT_SIZE = 0.46f; //percent from SCREEN_WEIDHT
    private float scoringScale;
    private float infoScale;

    public static final float FULL_JUMP_SIZE = 2.5f; //percent from SCREEN_HEIGHT
    public static final float JUMP_DECREASE = 0.1f; //percent from SCREEN_HEIGHT
    private float currentJumpSize; // текущее значение прыжка
    private float currentJumpDecrease;//снижение способности прыгать на эту величину

    public static final float GAME_SPEED_X = 0.7f; //скорость движения игры в процентах от SCREEN_WEIDHT
    public float currentGameSpeedX;
    private final int speedDecrease = 1; //снижение скорости когда сталкиваешься с ядом. В пикселях потому что 1 будет одинаково для всех разрешений


    public static final float DISTANCE_BETWEEN_GROUND = 120f; //расстояние между площадками в процентах от SCREEN_WEIDHT
    public static final float DISTANCE_BETWEEN_POISON = 139f; //расстояние между ядом в процентах от SCREEN_WEIDHT
    public static final float DISTANCE_BETWEEN_HEALTH = 185f; //расстояние между здоровьем в процентах от SCREEN_WEIDHT
    private float distanceBetweenGround; // а это в пикселях
    private float distanceBetweenPoison;
    private float distanceBetweenHealth;


    public static final int DELAY_BETWEEN_CHARACTER_ACTIONS = 5;
    public static final int NUMBER_OF_CHARACTERS_STATES = 8;
    SpriteBatch batch;
    Texture background;
    Texture gameOverTexture;
    Health health;
    Texture[] bird = new Texture[NUMBER_OF_CHARACTERS_STATES];
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    private int characterState;
    private float birdY;
    private float birdX;
    BitmapFont scoringFont;
    BitmapFont infoFont;

    private final int numberOfRoofs = 4;
    private final int numberOfPoison = 4;

    Poison[] poison = new Poison[numberOfPoison];
    Ground[] ground = new Ground[numberOfPoison];


    boolean gameIsRunning;
    boolean gameOver;
    float descentVelocity;

    private int progressCounter;
    private int currentCharacterDelay;


    private IActivityRequestHandler myRequestHandler;

    // Добавляем конструктор
    public JumpingCat(IActivityRequestHandler handler) {
        myRequestHandler = handler;
    }

    public void init() {

        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        SCREEN_WEIDHT = Gdx.graphics.getWidth();
        SCREEN_DIMENSION = SCREEN_WEIDHT * SCREEN_HEIGHT;

        currentJumpDecrease = JUMP_DECREASE * SCREEN_HEIGHT / 100; //снижение способности прыгать в пикселях
        currentJumpSize = FULL_JUMP_SIZE *SCREEN_HEIGHT / 100;//размер прыжка в пикселях

        currentGameSpeedX = GAME_SPEED_X * SCREEN_WEIDHT / 100; //  перевод в пиксели
        distanceBetweenGround = DISTANCE_BETWEEN_GROUND * SCREEN_WEIDHT / 100 ;
        distanceBetweenPoison = DISTANCE_BETWEEN_POISON * SCREEN_WEIDHT  / 100;
        distanceBetweenHealth = DISTANCE_BETWEEN_HEALTH * SCREEN_WEIDHT / 100;
        scoringScale = SCORING_TEXT_SIZE * SCREEN_WEIDHT / 100;
        infoScale = INFO_TEXT_SIZE * SCREEN_WEIDHT / 100;

        gameIsRunning = false;
        gameOver = false;
        descentVelocity = 0;
        progressCounter = 0;


        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        scoringFont = new BitmapFont();
        infoFont = new BitmapFont();

        scoringFont.setColor(Color.WHITE);
        infoFont.setColor(Color.WHITE);

        scoringFont.getData().setScale(scoringScale);
        infoFont.getData().setScale(infoScale);



        batch = new SpriteBatch();
        background = new Texture("bg.png");


        for (int i = 0; i < numberOfPoison; i++) {
            poison[i] = new Poison("food.png");
            poison[i].setX(i * distanceBetweenPoison).setY(Poison.getRandomY());
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
        birdY = Gdx.graphics.getHeight() / 2 - bird[characterState].getHeight() / 2;
        birdX = Gdx.graphics.getWidth() / 2 - bird[characterState].getWidth() / 2;


        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i] = new Ground("ground.png");
            ground[i].setX(i * distanceBetweenGround);
            ground[i].setY(Ground.getRandomY(ground[i]));
            if ( i == 0 ) {
                ground[i].setY((int) (birdY - ground[i].getHeight()));
            }

        }
        gameOverTexture = new Texture("gameover.png");

        myRequestHandler.showAdMob(true);
    }

    @Override
    public void create() {
        init();
    }


    @Override
    public void render() {
        myRequestHandler.showAdMob(true);


        if (gameIsRunning) {

//            //прыгать всегда
//            if (Gdx.input.justTouched()) {
//                descentVelocity = -currentJumpSize;
//                birdY -= descentVelocity;
//
//            }

            //если птица выше нижнего экрана
            if (birdY > 0 || descentVelocity < 0) {

                //чтобы не велезать за верхнюю часть экрана
//                if (birdY + bird[characterState].getHeight() >= Gdx.graphics.getHeight()) {
//                    descentVelocity = 0;
//                }

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
                            descentVelocity = -currentJumpSize;
                            birdY -= descentVelocity;

                        }

                        if (!ground[i].getGroundCompleted()){
                            ground[i].setGroundCompleted(true);
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
                    if(characterState == NUMBER_OF_CHARACTERS_STATES - 1) {
                        characterState = 0;
                    }
                    //иначе устанавливаем следующее состояние
                    else if (characterState == i) {
                        characterState = i + 1;
                        break;
                    }

                }

            }

            health.decreaseX(currentGameSpeedX);
            //если health ушло за границы экрана
            if (health.getX() + health.getWidth() < 0) {
                health.setX(distanceBetweenHealth);
                health.setY(Gdx.graphics.getHeight() / 2);
                health.setCollisionWithCharacter(false);
            }


            //крыши бегут непрерывно
            for (int i = 0; i < numberOfRoofs; i++) {
                ground[i].decreaseX(currentGameSpeedX);

                //если крыша полностью ушла за экран то вместо нее рисуем новую после последней крыши
                if (ground[i].getX() + ground[i].getWidth() < 0) {
                    ground[i].setX(ground[numberOfRoofs - 1].getX() + (i + 1) * distanceBetweenGround);
                    ground[i].setY(Ground.getRandomY(ground[i]));
                    ground[i].setGroundCompleted(false);

                }

            }
            Gdx.app.log("Progress", "" + progressCounter);
            Gdx.app.log("JumpSize", "" + currentJumpSize);




            //если eда ушла за экран то вместо нее рисуем новую после последней еды
            for (int i = 0; i < numberOfPoison; i++) {
                poison[i].decreaseX(currentGameSpeedX);

                if (poison[i].getX() + poison[i].getHeight() < 0) {
                    poison[i]
                            .setX(poison[numberOfPoison - 1].getX() + (i + 1) * distanceBetweenPoison)
                            .setY(Poison.getRandomY())
                            .setCollisionWithCharacter(false);
                }
            }


            //столкновение с едой
            for (int i = 0; i < numberOfPoison; i++) {

                if (Intersector.overlaps(birdCircle, poison[i].getCircleBounds())) {
                    if (!poison[i].getCollisionWithCharacter()) {

                        currentJumpSize -= currentJumpDecrease;
                        currentGameSpeedX -= speedDecrease;
                        poison[i].setCollisionWithCharacter(true);
                    }

                }
            }

            //столкновение с health
            if (Intersector.overlaps(birdCircle, health.getCircleBounds())) {
                if (!health.getCollisionWithCharacter()) {

                    currentJumpSize += currentJumpDecrease;
                    currentGameSpeedX += speedDecrease;
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
                    myRequestHandler.showAdMobInterstitial();

                    init();
                }

            }
        }

        birdCircle.set(birdX + bird[characterState].getWidth() / 2,
                birdY + bird[characterState].getHeight() / 2, bird[characterState].getWidth() / 2);


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        for (int i = 0; i < numberOfRoofs; i++) {
            batch.draw(ground[i], ground[i].getX(), ground[i].getY(), ground[i].getWidth(), ground[i].getHeight());
            ground[i].setRectangleBounds(ground[i].getX(), ground[i].getY(), ground[i].getWidth(), ground[i].getHeight());
        }

        for (int i = 0; i < numberOfPoison; i++) {
            if (!poison[i].getCollisionWithCharacter()) {
                batch.draw(poison[i], poison[i].getX(), poison[i].getY(), poison[i].getWidth(), poison[i].getHeight());
            }
            poison[i].setCircleBounds(poison[i].getX() + poison[i].getWidth() / 2,
                    poison[i].getY() + poison[i].getHeight() / 2,
                    poison[i].getHeight() / 2);

        }
        health.setCircleBounds(health.getX() + health.getWidth() / 2, health.getY() + health.getHeight() / 2, health.getHeight() / 2);

        if (!health.getCollisionWithCharacter()) {
            batch.draw(health, health.getX(), health.getY(), health.getWidth(), health.getHeight());
        }

        //отступы указаны в процентах
        scoringFont.draw(batch, String.valueOf(progressCounter), (9.2f * SCREEN_WEIDHT) / 100, 10.4f * SCREEN_HEIGHT / 100);
        infoFont.draw(batch, "Jump " + String.valueOf((int) (currentJumpSize * 100 / (SCREEN_HEIGHT * FULL_JUMP_SIZE / 100))), Gdx.graphics.getWidth() - (32.4f * SCREEN_WEIDHT / 100), Gdx.graphics.getHeight() - (2.6f * SCREEN_HEIGHT / 100));
        infoFont.draw(batch, "Speed " + String.valueOf((int) (currentGameSpeedX * 100 / (SCREEN_WEIDHT * GAME_SPEED_X / 100))), 4.6f * SCREEN_WEIDHT / 100, Gdx.graphics.getHeight() - (2.6f * SCREEN_HEIGHT / 100));

        batch.draw(bird[characterState],
                birdX,
                birdY,
                bird[characterState].getWidth(), bird[characterState].getHeight());

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
//        for (int i = 0; i < numberOfPoison; i++) {
//            shapeRenderer.circle(poison[i].getCircleBounds().x, poison[i].getCircleBounds().y, poison[i].getCircleBounds().radius);
//
//        }
//        shapeRenderer.end();

    }


}
