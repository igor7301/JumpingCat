package com.jumping.jumpingcat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class JumpingCat extends ApplicationAdapter {


    public static final String JUMP_TEXT = "Jump";
    public static final String SPEED_TEXT = "Speed";
    private static final String SCORE_TEXT = "SCORE";
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WEIDHT;
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
    public int currentGameSpeedX;
    private int speedDecrease; //снижение скорости когда сталкиваешься с ядом.


    public static final float DISTANCE_BETWEEN_GROUND = 120f; //расстояние между площадками в процентах от SCREEN_WEIDHT
    public static final float DISTANCE_BETWEEN_POISON = 139f; //расстояние между ядом в процентах от SCREEN_WEIDHT
    public static final float DISTANCE_BETWEEN_HEALTH = 185f; //расстояние между здоровьем в процентах от SCREEN_WEIDHT
    private float distanceBetweenGround; // а это в пикселях
    private float distanceBetweenPoison;
    private float distanceBetweenHealth;

    public static final float DESCENT_VELOCITY = 0.056f; //скорость падения героя в процентах SCREEN_HEIGHT
    float descentVelocity; // в пикселях

    public static final int DELAY_BETWEEN_HERO_ACTIONS = 5;
    public static final int NUMBER_OF_HERO_STATES = 4;
    SpriteBatch batch;
    TextureRegion background;
    TextureRegion gameOverTexture;
    Health health;
    TextureRegion[] hero = new TextureRegion[NUMBER_OF_HERO_STATES];
    ShapeRenderer shapeRenderer;
    Circle heroCircle;
    private int heroState;
    private float heroY;
    private float heroX;
    BitmapFont scoringFont;
    BitmapFont infoFont;

    private final int numberOfRoofs = 2;
    private final int numberOfPoison = 4;

    Poison[] poison = new Poison[numberOfPoison];
    Ground[] ground = new Ground[numberOfPoison];


    boolean gameIsRunning;
    boolean gameOver;

    private int progressCounter;
    private int currentCharacterDelay;


    private IActivityRequestHandler myRequestHandler;

    // Добавляем конструктор
    public JumpingCat(IActivityRequestHandler handler) {
        myRequestHandler = handler;
    }

    public void init() {

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("atlas.pack"));

        SCREEN_HEIGHT = Gdx.graphics.getHeight();
//        SCREEN_HEIGHT = 1270;
        SCREEN_WEIDHT = Gdx.graphics.getWidth();
//        SCREEN_WEIDHT = 820;
        SCREEN_DIMENSION = SCREEN_WEIDHT * SCREEN_HEIGHT;

        currentJumpDecrease = JUMP_DECREASE * SCREEN_HEIGHT / 100; //снижение способности прыгать в пикселях
        currentJumpSize = FULL_JUMP_SIZE * SCREEN_HEIGHT / 100;//размер прыжка в пикселях

        currentGameSpeedX = (int) (GAME_SPEED_X * SCREEN_WEIDHT / 100); //  перевод в пиксели
        distanceBetweenGround = DISTANCE_BETWEEN_GROUND * SCREEN_WEIDHT / 100;
        distanceBetweenPoison = DISTANCE_BETWEEN_POISON * SCREEN_WEIDHT / 100;
        distanceBetweenHealth = DISTANCE_BETWEEN_HEALTH * SCREEN_WEIDHT / 100;
        scoringScale = SCORING_TEXT_SIZE * SCREEN_WEIDHT / 100;
        infoScale = INFO_TEXT_SIZE * SCREEN_WEIDHT / 100;
        speedDecrease = 1;

        gameIsRunning = false;
        gameOver = false;
        descentVelocity = 0;
        progressCounter = 0;


        shapeRenderer = new ShapeRenderer();
        heroCircle = new Circle();
        scoringFont = new BitmapFont();
        infoFont = new BitmapFont();

        scoringFont.setColor(Color.WHITE);
        infoFont.setColor(Color.WHITE);

        scoringFont.getData().setScale(scoringScale);
        infoFont.getData().setScale(infoScale);


        batch = new SpriteBatch();
        background = textureAtlas.findRegion("bg");


        for (int i = 0; i < numberOfPoison; i++) {
            poison[i] = new Poison(textureAtlas.findRegion("food"));
            poison[i].setX(i * distanceBetweenPoison).setY(Poison.getRandomY(SCREEN_HEIGHT));
        }

        health = new Health(textureAtlas.findRegion("health"));
        health.setX(distanceBetweenHealth);
        health.setY(SCREEN_HEIGHT / 2);


        hero[0] = textureAtlas.findRegion("panda1");
        hero[1] = textureAtlas.findRegion("panda2");
        hero[2] = textureAtlas.findRegion("panda3");
        hero[3] = textureAtlas.findRegion("panda4");

        heroY = SCREEN_HEIGHT / 2 - hero[heroState].getRegionHeight() / 2;
        heroX = SCREEN_WEIDHT / 2 - hero[heroState].getRegionWidth() / 2;


        for (int i = 0; i < numberOfRoofs; i++) {




            ground[i] = new Ground(textureAtlas.findRegion("ground"));

            ground[i].setX(i * distanceBetweenGround);
            ground[i].setY(Ground.getRandomY(SCREEN_HEIGHT));
            if (i == 0) {
                ground[i].setY((int) (heroY - ground[i].getHeight()));
            }

        }
        gameOverTexture = textureAtlas.findRegion("gameover");

        myRequestHandler.showAdMob(true);






    }

    @Override
    public void create() {
        init();
        myRequestHandler.showAdMob(true);
    }


    @Override
    public void render() {


        if (gameIsRunning) {

//            //прыгать всегда
//            if (Gdx.input.justTouched()) {
//                descentVelocity = -currentJumpSize;
//                heroY -= descentVelocity;
//
//            }

            //если птица выше нижнего экрана
            if (heroY > 0 || descentVelocity < 0) {

                //чтобы не велезать за верхнюю часть экрана
//                if (heroY + hero[heroState].getHeight() >= Gdx.graphics.getHeight()) {
//                    descentVelocity = 0;
//                }

                //птица падает

                descentVelocity = descentVelocity + DESCENT_VELOCITY * SCREEN_HEIGHT / 100;
                heroY -= descentVelocity;

                for (int i = 0; i < numberOfRoofs; i++) {

                    float roofTopY = ground[i].getRectangleBounds().y + ground[i].getRectangleBounds().height;
                    //если птица касается крыши сверху
                    if (Intersector.overlaps(heroCircle, ground[i].getRectangleBounds())
                            && heroCircle.y >= roofTopY && heroCircle.x > ground[i].getRectangleBounds().x) {

                        //то она остается на крыше
                        descentVelocity = 0;
                        heroY = roofTopY - 1; //чтобы персонаж был чуть ниже области и всегда пересекался с ней

                        //прыгать можно только если птица стоит на крыше
                        if (Gdx.input.justTouched()) {

                            descentVelocity = -currentJumpSize;
                            heroY -= descentVelocity;
                            Gdx.app.log("JUMP", "" + descentVelocity);

                        }

                        if (!ground[i].getGroundCompleted()) {
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
            if (currentCharacterDelay < DELAY_BETWEEN_HERO_ACTIONS) {
                currentCharacterDelay++;
            } else {
                currentCharacterDelay = 0;
                for (int i = 0; i < NUMBER_OF_HERO_STATES; i++) {
                    //если это последнее возможное состояние
                    if (heroState == NUMBER_OF_HERO_STATES - 1) {
                        heroState = 0;
                    }
                    //иначе устанавливаем следующее состояние
                    else if (heroState == i) {
                        heroState = i + 1;
                        break;
                    }

                }

            }

            health.decreaseX(currentGameSpeedX);
            //если health ушло за границы экрана
            if (health.getX() + health.getWidth() < 0) {
                health.setX(distanceBetweenHealth);
                health.setY(SCREEN_HEIGHT / 2);
                health.setCollisionWithCharacter(false);
            }


            //крыши бегут непрерывно
            for (int i = 0; i < numberOfRoofs; i++) {
                ground[i].decreaseX(currentGameSpeedX);

                //если крыша полностью ушла за экран то вместо нее рисуем новую после последней крыши
                if (ground[i].getX() + ground[i].getWidth() < 0) {
                    ground[i].setX(ground[numberOfRoofs - 1].getX() + (i + 1) * distanceBetweenGround);
                    ground[i].setY(Ground.getRandomY(SCREEN_HEIGHT));
                    ground[i].setGroundCompleted(false);

                }

            }
            Gdx.app.log("Progress", "" + progressCounter);
            Gdx.app.log("JumpSize", "" + currentJumpSize);
            Gdx.app.log("Speed", "" + currentGameSpeedX);


            //если eда ушла за экран то вместо нее рисуем новую после последней еды
            for (int i = 0; i < numberOfPoison; i++) {
                poison[i].decreaseX(currentGameSpeedX);

                if (poison[i].getX() + poison[i].getHeight() < 0) {
                    poison[i]
                            .setX(poison[numberOfPoison - 1].getX() + (i + 1) * distanceBetweenPoison)
                            .setY(Poison.getRandomY(SCREEN_HEIGHT))
                            .setCollisionWithCharacter(false);
                }
            }


            //столкновение с едой
            for (int i = 0; i < numberOfPoison; i++) {

                if (Intersector.overlaps(heroCircle, poison[i].getCircleBounds())) {
                    if (!poison[i].getCollisionWithCharacter()) {

                        currentJumpSize -= currentJumpDecrease;
                        currentGameSpeedX -= speedDecrease;
                        poison[i].setCollisionWithCharacter(true);
                    }

                }
            }

            //столкновение с health
            if (Intersector.overlaps(heroCircle, health.getCircleBounds())) {
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

        heroCircle.set(heroX + 0.25f * SCREEN_WEIDHT / 2,
                heroY + 0.094f * SCREEN_HEIGHT / 2, 0.094f * SCREEN_HEIGHT / 2);




        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.disableBlending();
        batch.draw(background, 0, 0, SCREEN_WEIDHT, SCREEN_HEIGHT);

        batch.enableBlending();


        for (int i = 0; i < numberOfRoofs; i++) {
            batch.draw(ground[i].getTextureRegion(), ground[i].getX(), ground[i].getY(), 0.83f * SCREEN_WEIDHT, 0.62f * SCREEN_HEIGHT);
            ground[i].setRectangleBounds(ground[i].getX(), ground[i].getY(), 0.83f * SCREEN_WEIDHT,  0.62f * SCREEN_HEIGHT);
        }

        for (int i = 0; i < numberOfPoison; i++) {
            if (!poison[i].getCollisionWithCharacter()) {
                batch.draw(poison[i].getTextureRegion(), poison[i].getX(), poison[i].getY(), 0.13f * SCREEN_WEIDHT, 0.085f * SCREEN_HEIGHT);
            }
            poison[i].setCircleBounds(poison[i].getX() + 0.13f * SCREEN_WEIDHT / 2,
                    poison[i].getY() + 0.085f * SCREEN_HEIGHT / 2,
                    0.085f * SCREEN_HEIGHT / 2);

        }
        health.setCircleBounds(health.getX() + 0.166f * SCREEN_WEIDHT / 2, health.getY() + 0.079f * SCREEN_HEIGHT / 2, 0.079f * SCREEN_HEIGHT / 2);

        if (!health.getCollisionWithCharacter()) {
            batch.draw(health.getTextureRegion(), health.getX(), health.getY(), 0.166f * SCREEN_WEIDHT, 0.079f * SCREEN_HEIGHT);
        }



        batch.draw(hero[heroState],
                heroX,
                heroY,
                0.25f * SCREEN_WEIDHT, 0.094f * SCREEN_HEIGHT);

        if (gameOver) {
            batch.draw(gameOverTexture, SCREEN_WEIDHT / 2 - gameOverTexture.getRegionWidth() / 2,
                    SCREEN_HEIGHT / 2 - gameOverTexture.getRegionHeight() / 2);
            infoFont.draw(batch, SCORE_TEXT + " " + String.valueOf(progressCounter), 35f * SCREEN_WEIDHT / 100, 30f * SCREEN_HEIGHT / 100);
        }
        else {

            //отступы указаны в процентах
            scoringFont.draw(batch, String.valueOf(progressCounter), (9.2f * SCREEN_WEIDHT) / 100, 10.4f * SCREEN_HEIGHT / 100);
            infoFont.draw(batch, JUMP_TEXT + " " + String.valueOf((int) (currentJumpSize * 100 / (SCREEN_HEIGHT * FULL_JUMP_SIZE / 100))), SCREEN_WEIDHT - (32.4f * SCREEN_WEIDHT / 100), SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));
            infoFont.draw(batch, SPEED_TEXT + " " + String.valueOf((int) (currentGameSpeedX * 100 / (SCREEN_WEIDHT * GAME_SPEED_X / 100))), 4.6f * SCREEN_WEIDHT / 100, SCREEN_HEIGHT - (2.6f * SCREEN_HEIGHT / 100));
        }

        batch.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(heroCircle.x, heroCircle.y, heroCircle.radius);
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
