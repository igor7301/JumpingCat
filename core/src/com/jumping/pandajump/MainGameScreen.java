package com.jumping.pandajump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainGameScreen implements Screen, InputProcessor {

    private OrthographicCamera cam;
    private final StretchViewport viewport;
    private Stage stage;
    private TextureAtlas textureAtlas;
    private float currentJumpSize; // текущее значение прыжка
    private float currentJumpDecrease;//снижение способности прыгать на эту величину
    public float currentGameSpeedX;
    private float speedDecrease; //снижение скорости когда сталкиваешься с ядом.
    private float distanceBetweenGround; // а это в пикселях
    private float distanceBetweenPoison;
    private float distanceBetweenHealth;
    private float descentVelocity; // в пикселях
    private SpriteBatch batch;
    private BackGround background;
    private TextOnScreenActor textOnScreenActor;
    private Health health;
    private Hero hero;
    private ShapeRenderer shapeRenderer;
    private final int numberOfRoofs = 3;
    private final int numberOfPoison = 4;
    private Poison[] poison = new Poison[numberOfPoison];
    private Ground[] ground = new Ground[numberOfPoison];
    boolean gameIsRunning;
    boolean gameOver;
    private int progressCounter;
    private IActivityRequestHandler myRequestHandler;
    private int numberClickOnAd;
    private boolean gameWin;
    private Game game;


    public IActivityRequestHandler getMyRequestHandler() {
        return myRequestHandler;
    }

    public int getAmountOfCountinueWithAdvertising() {
        return Constants.amountOfCountinueWithAdvertising;
    }

    public int getNumberClickOnAd() {
        return numberClickOnAd;
    }

    public int getProgressCounter() {
        return progressCounter;
    }

    public boolean isGameWin() {
        return gameWin;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void init() {
        currentJumpDecrease = Constants.JUMP_DECREASE * Constants.SCREEN_HEIGHT / 100; //снижение способности прыгать в пикселях
        currentJumpSize = Constants.FULL_JUMP_SIZE * Constants.SCREEN_HEIGHT / 100;//размер прыжка в пикселях
        currentGameSpeedX = (int) (Constants.GAME_SPEED_X * Constants.SCREEN_WEIDHT / 100); //  перевод в пиксели
        distanceBetweenGround = Constants.DISTANCE_BETWEEN_GROUND * Constants.SCREEN_WEIDHT / 100;
        distanceBetweenPoison = Constants.DISTANCE_BETWEEN_POISON * Constants.SCREEN_WEIDHT / 100;
        distanceBetweenHealth = Constants.DISTANCE_BETWEEN_HEALTH * Constants.SCREEN_WEIDHT / 100;
        speedDecrease = (0.09f * Constants.SCREEN_WEIDHT / 100);
        numberClickOnAd = 0;
        gameIsRunning = false;
        gameOver = false;
        descentVelocity = 0;
        progressCounter = 0;
        gameWin = false;

        for (int i = 0; i < numberOfPoison; i++) {
            poison[i].setX(i * distanceBetweenPoison);
            poison[i].setY(Poison.getRandomY(Constants.SCREEN_HEIGHT));
        }


        health.setX(distanceBetweenHealth);
        health.setY(Constants.SCREEN_HEIGHT / 2);

        hero.setY(Constants.SCREEN_HEIGHT / 2 - hero.getTextureRegion().getRegionHeight() / 2);
        hero.setX( Constants.SCREEN_WEIDHT / 2 - hero.getTextureRegion().getRegionWidth() / 2);

        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i].setGroundCompleted(false);
            ground[i].setX(i * distanceBetweenGround);
            ground[i].setY(Ground.getRandomY(Constants.SCREEN_HEIGHT));
            if (i == 0) {
                ground[i].setY((int) (hero.getY() - 0.7f * Constants.SCREEN_HEIGHT));
            }
        }

        textOnScreenActor.setSpeedForDisplay(100);
        textOnScreenActor.setJumpForDisplay(100);
    }


    public void initContinueFromLastPoint() {

        float offsetX = Math.abs(hero.getX() - ground[0].getX());
        for (int i = 0; i < numberOfRoofs; i++) {
            if (Math.abs(hero.getX() - ground[i].getX()) <= offsetX) {
                offsetX = Math.abs(hero.getX() - ground[i].getX());
            }
        }

        hero.setY(Constants.SCREEN_HEIGHT);

        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i].setX(ground[i].getX() - offsetX);
        }

        for (int i = 0; i < numberOfPoison; i++) {
            poison[i].setX(poison[i].getX() - offsetX);
        }
        health.setX(health.getX() - offsetX);
    }

    @Override
    public void resume() {
        //super.resume();

        //продолжаем игру
        if (myRequestHandler.doesUserClickOnAd() && numberClickOnAd < Constants.amountOfCountinueWithAdvertising) {

            gameOver = false;
            //gameIsRunning = true;
            numberClickOnAd++;
            initContinueFromLastPoint();
        } else {
            myRequestHandler.setUserClickOnAd(false);
            init();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();

    }


    public MainGameScreen(final MyMainGame game) {
        this.game = game;
        myRequestHandler  = game.getRequestHandler();

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        //сцена своя для каждого экрана
        viewport = new StretchViewport(Constants.SCREEN_WEIDHT, Constants.SCREEN_HEIGHT, cam);
        stage = new Stage(viewport, game.getSpriteBatch());
        //stage.act(Gdx.graphics.getDeltaTime());

        //batch один на всю игру
        batch = game.getSpriteBatch();



        textureAtlas = new TextureAtlas(Gdx.files.internal("atlas.pack"));
        shapeRenderer = new ShapeRenderer();
        background = new BackGround(textureAtlas.findRegion("bg"));
        stage.addActor(background);
        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i] = new Ground(textureAtlas.findRegion("ground"));
            stage.addActor(ground[i]);

        }
        for (int i = 0; i < numberOfPoison; i++) {
            poison[i] = new Poison(textureAtlas.findRegion("food"));
            stage.addActor(poison[i]);
        }
        health = new Health(textureAtlas.findRegion("health"));
        stage.addActor(health);

        TextureRegion[] heroTexture =  {textureAtlas.findRegion("panda1"),
                textureAtlas.findRegion("panda2"),
                textureAtlas.findRegion("panda3"),
                textureAtlas.findRegion("panda4")};

        hero =  new Hero(heroTexture[0]);
                hero.setAnimation(new Animation(5f, heroTexture));

        stage.addActor(hero);

        textOnScreenActor = new TextOnScreenActor(textureAtlas.findRegion("gameover"), this);

        stage.addActor(textOnScreenActor);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        init();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        if (gameIsRunning) {

//            //прыгать всегда
//            if (Gdx.input.justTouched()) {
//                descentVelocity = -currentJumpSize;
//                hero.getY() -= descentVelocity;
//
//            }

            //если птица выше нижнего экрана
            if (hero.getY() > 0 || descentVelocity < 0) {

                //чтобы не велезать за верхнюю часть экрана
//                if (hero.getY() + hero[heroState].getHeight() >= Gdx.graphics.getHeight()) {
//                    descentVelocity = 0;
//                }

                //птица падает

                descentVelocity = descentVelocity + Constants.DESCENT_VELOCITY * Constants.SCREEN_HEIGHT / 100;
                hero.setY(hero.getY() - descentVelocity);

                for (int i = 0; i < numberOfRoofs; i++) {

                    float roofTopY = ground[i].getRectangleBounds().y + ground[i].getRectangleBounds().height;
                    //если птица касается крыши сверху
                    if (Intersector.overlaps(hero.getCircleBounds(), ground[i].getRectangleBounds())
                            && hero.getCircleBounds().y >= roofTopY && hero.getCircleBounds().x > ground[i].getRectangleBounds().x) {

                        //то она остается на крыше
                        descentVelocity = 0;
                        hero.setY(roofTopY - 1); //чтобы персонаж был чуть ниже области и всегда пересекался с ней

                        //прыгать можно только если птица стоит на крыше
                        if (Gdx.input.justTouched()) {

                            descentVelocity = -currentJumpSize;
                            hero.setY(hero.getY() - descentVelocity);
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



            health.setX(health.getX() - currentGameSpeedX);
//        если health ушло за границы экрана
        if (health.getX() + health.getTextureRegion().getRegionWidth() < 0) {
            health.setX(distanceBetweenHealth);
            health.setY(Constants.SCREEN_HEIGHT / 2);
            health.setCollisionWithCharacter(false);
        }


       //крыши бегут непрерывно
        for (int i = 0; i < numberOfRoofs; i++) {
            ground[i].setX(ground[i].getX() - currentGameSpeedX);

            //если крыша полностью ушла за экран то вместо нее рисуем новую после последней крыши
            if (ground[i].getX() + ground[i].getTextureRegion().getRegionWidth() < 0) {
                ground[i].setX(ground[numberOfRoofs - 1].getX() + (i + 1) * distanceBetweenGround);
                ground[i].setY(Ground.getRandomY(Constants.SCREEN_HEIGHT));
                ground[i].setGroundCompleted(false);

            }

        }
//            Gdx.app.log("Progress", "" + progressCounter);
//            Gdx.app.log("JumpSize", "" + currentJumpSize);
//            Gdx.app.log("Speed", "" + currentGameSpeedX);
//

//                если eда ушла за экран то вместо нее рисуем новую после последней еды
        for (int i = 0; i < numberOfPoison; i++) {
            poison[i].setX(poison[i].getX()  - currentGameSpeedX);

            if (poison[i].getX() + poison[i].getTextureRegion().getRegionWidth() < 0) {
                poison[i]
                        .setX(poison[numberOfPoison - 1].getX() + (i + 1) * distanceBetweenPoison);
                poison[i].setY(Poison.getRandomY(Constants.SCREEN_HEIGHT));
                poison[i]  .setCollisionWithCharacter(false);
            }
        }


            //столкновение с едой
            for (int i = 0; i < numberOfPoison; i++) {

                if (Intersector.overlaps(hero.getCircleBounds(), poison[i].getCircleBounds())) {
                    if (!poison[i].getCollisionWithCharacter()) {

                        currentJumpSize -= currentJumpDecrease;
                        currentGameSpeedX -= speedDecrease;
                        poison[i].setCollisionWithCharacter(true);
                        textOnScreenActor.decreaseDisplaySpeed();
                        textOnScreenActor.decreaseDisplayJump();
                    }

                }
            }

            //столкновение с health
            if (Intersector.overlaps(hero.getCircleBounds(), health.getCircleBounds())) {
                if (!health.getCollisionWithCharacter()) {

                    currentJumpSize += currentJumpDecrease;
                    currentGameSpeedX += speedDecrease;
                    health.setCollisionWithCharacter(true);
                        textOnScreenActor.increaseDisplaySpeed();
                        textOnScreenActor.increaseDisplayJump();
                }

            }


        } else {
//
//            if (gameOver) {
//                    myRequestHandler.showAdMobInterstitial();
//
//            }
            if (Gdx.input.justTouched()) {

                //если игра не закончена
                if (!gameOver) {
                    gameIsRunning = true;

                } else {
                    init();
                    //игра закончена показываем рекламу

                   // myRequestHandler.showAdMobInterstitial();

                    //возомновлем игру лмбо сначала либо стобого мета где закончили  методе resume()
                    //если юзер нажал на рекламу (но не более одного раза за активную игру)
                }

            }



        }


//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        stage.getBatch().begin();
        //stage.getBatch().disableBlending();
        //stage.getBatch().draw(background, 0, 0, SCREEN_WEIDHT, SCREEN_HEIGHT);

        if (progressCounter == Constants.MAX_PROGRESS_NUMBER) {
            gameIsRunning = false;
            gameOver = true;
            gameWin = true;
        }

//        try {
//            switch (progressCounter) {
//
//                case LEVEL2_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg2.png");
//                    break;
//                case LEVEL3_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg3.png");
//                    break;
//                case LEVEL4_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg4.png");
//                    break;
//                case LEVEL5_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg5.png");
//                    break;
//                case LEVEL6_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg6.png");
//                    break;
//                case LEVEL7_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg7.png");
//                    break;
//                case LEVEL8_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg8.png");
//                    break;
//                case LEVEL9_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg9.png");
//                    break;
//                case LEVEL10_PROGRESS_NUMBER:
//                    background = TextureSingleton.getInstance("bg10.png");
//                    break;
//            }
//        } catch (Exception e) {
//            background = TextureSingleton.getInstance("bg.png");
//            ;
//        }


        //stage.getBatch().draw(background, 0, 0, SCREEN_WEIDHT, SCREEN_HEIGHT);

//        stage.getBatch().enableBlending();

        stage.draw();
        stage.act(delta);


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(hero.getCircleBounds().x, hero.getCircleBounds().y, hero.getCircleBounds().radius);
//        for (int i = 0; i < numberOfRoofs; i++) {
//            shapeRenderer.rect(ground[i].getRectangleBounds().x, ground[i].getRectangleBounds().y, ground[i].getRectangleBounds().width, ground[i].getRectangleBounds().height);
//
//        }
//        for (int i = 0; i < numberOfPoison; i++) {
//            shapeRenderer.circle(poison[i].getCircleBounds().x, poison[i].getCircleBounds().y, poison[i].getCircleBounds().radius);
//
//        }
//            shapeRenderer.circle(health.getCircleBounds().x, health.getCircleBounds().y, health.getCircleBounds().radius);
//        shapeRenderer.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }


    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
                game.setScreen(((MyMainGame) game).getStartScreen());
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
