package com.jumping.jumpingcat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class JumpingCat extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture health;
    Texture[] bird = new Texture[2];
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    private int birdState;
    private float birdY;
    private float birdX;

    private final int numberOfRoofs = 4;
    private final int numberOfFood = 4;
    Circle foodCircle[] = new Circle[numberOfFood];
    boolean foodCollision[] = new boolean[numberOfFood];
    Texture[] food = new Texture[numberOfFood];
    Texture[] roof = new Texture[numberOfFood];
    Rectangle[] roofRectangle = new Rectangle[numberOfRoofs];
    private float[] roofX = new float[numberOfRoofs];
    private float[] roofY = new float[numberOfRoofs];
    private float[] foodX = new float[numberOfRoofs];
    private float[] foodY = new float[numberOfRoofs];
    private final float roofOffsetVelocityX = 5;
    private float healthX;
    private float healthY;
    boolean[] roofCompleted = new boolean[numberOfRoofs];

    boolean gameIsRunning;
    boolean gameOver;
    float descentVelocity;
    private float jumpSize;
    private final int distanceBetweenRoof = 1200;
    private final int distanceBetweenFood = 900;
    private final int distanceBetweenHealth = 2000;
    private final int jumpDecrease = 2; //снижение способности прыгать когда ешь еду
    private Circle healthCircle;
    private boolean healthCollision;
    private int progressCounter;


    @Override
    public void create() {
        gameIsRunning = false;
        gameOver = false;
        healthCollision = false;
        descentVelocity = 0;
        jumpSize = 40f;
        progressCounter = 0;

        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        healthCircle = new Circle();

        batch = new SpriteBatch();
        background = new Texture("bg.png");

        for (int i = 0; i < numberOfRoofs; i++) {
            roof[i] = new Texture("roof.png");
            roofX[i] = i * distanceBetweenRoof;
            roofY[i] = new Random(System.currentTimeMillis()).nextInt(Gdx.graphics.getHeight() / 5);
            roofRectangle[i] = new Rectangle();
            roofCompleted[i] = false;

        }

        for (int i = 0; i < numberOfFood; i++) {
            food[i] = new Texture("food.png");
            foodX[i] = i * distanceBetweenFood;
            foodY[i] = getRandomFoodY();
            foodCircle[i] = new Circle();
            foodCollision[i] = false;
        }

        health = new Texture("health.png");
        healthX = distanceBetweenHealth;
        healthY = Gdx.graphics.getHeight() / 2;


        bird[0] = new Texture("bird.png");
        bird[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;
        birdX = Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2;


    }

    private int getRandomRoofY() {
        int minY = 0;
        int dispersionY = Gdx.graphics.getHeight() / 2;
        return new Random(System.currentTimeMillis()).nextInt(dispersionY) + minY;
    }

    private int getRandomFoodY() {
        int minY = Gdx.graphics.getHeight() / 2;
        int dispersionY = Gdx.graphics.getHeight() / 4;
        return minY + new Random(System.currentTimeMillis()).nextInt(dispersionY);
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

                    float roofTopY = roofRectangle[i].y + roofRectangle[i].height;
                    //если птица касается крыши сверху
                    if (Intersector.overlaps(birdCircle, roofRectangle[i])
                            && birdCircle.y >= roofTopY && birdCircle.x > roofRectangle[i].x) {

                        //то она остается на крыше
                        descentVelocity = 0;
                        birdY = roofTopY;

                        //прыгать можно только если птица стоит на крыше
                        if (Gdx.input.justTouched()) {
                            descentVelocity = -jumpSize;
                            birdY -= descentVelocity;

                        }

                        if (!roofCompleted[i]){
                           roofCompleted[i] = true;
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
            if (birdState == 0) {
                birdState = 1;
            } else {
                birdState = 0;
            }


            healthX -= roofOffsetVelocityX;
            if (healthX + health.getWidth() < 0) {
                healthX = distanceBetweenHealth;
                healthY = Gdx.graphics.getHeight() / 2;
                healthCollision = false;
            }


            //крыши бегут непрерывно
            for (int i = 0; i < numberOfRoofs; i++) {
                roofX[i] -= roofOffsetVelocityX;

                //если крыша полностью ушла за экран то вместо нее рисуем новую после последней крыши
                if (roofX[i] + roof[i].getWidth() < 0) {
                    roofX[i] = roofX[numberOfRoofs - 1] + (i + 1) * distanceBetweenRoof;
                    roofY[i] = getRandomRoofY();
                    roofCompleted[i] = false;

                }





            }
            Gdx.app.log("Progress", "" + progressCounter);




            //если eда ушла за экран то вместо нее рисуем новую после последней еды
            for (int i = 0; i < numberOfFood; i++) {
                foodX[i] -= roofOffsetVelocityX;

                if (foodX[i] + food[i].getHeight() < 0) {
                    foodX[i] = foodX[numberOfFood - 1] + (i + 1) * distanceBetweenFood;
                    foodY[i] = getRandomFoodY();
                    foodCollision[i] = false;
                }
            }


            //столкновение с едой
            for (int i = 0; i < numberOfFood; i++) {

                if (Intersector.overlaps(birdCircle, foodCircle[i])) {
                    if (!foodCollision[i]) {

                        jumpSize -= jumpDecrease;
                        foodCollision[i] = true;
                    }

                }
            }

            //столкновение с health
            if (Intersector.overlaps(birdCircle, healthCircle)) {
                if (!healthCollision) {

                    jumpSize += jumpDecrease;
                    healthCollision = true;
                }

            }

            Gdx.app.log("Food", "" + jumpSize);

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
            batch.draw(roof[i], roofX[i], roofY[i], roof[i].getWidth(), roof[i].getHeight());
            roofRectangle[i].set(roofX[i], roofY[i], roof[i].getWidth(), roof[i].getHeight());
        }

        for (int i = 0; i < numberOfFood; i++) {
            if (!foodCollision[i]) {
                batch.draw(food[i], foodX[i], foodY[i], food[i].getWidth(), food[i].getHeight());
            }
            foodCircle[i].set(foodX[i] + food[i].getWidth() / 2,
                    foodY[i] + food[i].getHeight() / 2,
                    food[i].getHeight() / 2);

        }
        healthCircle.set(healthX + health.getWidth() / 2, healthY + health.getHeight() / 2, health.getHeight() / 2);

        if (!healthCollision) {
            batch.draw(health, healthX, healthY, health.getWidth(), health.getHeight());
        }
        batch.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//        for (int i = 0; i < numberOfRoofs; i++) {
//            shapeRenderer.rect(roofRectangle[i].x, roofRectangle[i].y, roofRectangle[i].width, roofRectangle[i].height);
//
//        }
//        for (int i = 0; i < numberOfFood; i++) {
//            shapeRenderer.circle(foodCircle[i].x, foodCircle[i].y, foodCircle[i].radius);
//
//        }
//        shapeRenderer.end();

    }


}
