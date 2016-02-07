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
    Texture[] bird = new Texture[2];
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    private int birdState;
    private float birdY;
    private float birdX;

    private int numberOfRoofs = 4;
    private int numberOfFood = 4;
    Circle foodCircle[] = new Circle[numberOfFood];
    boolean foodCollision[] = new boolean[numberOfFood];
    Texture[] food = new Texture[numberOfFood];
    Texture[] roof = new Texture[numberOfFood];
    Rectangle[] roofRectangle = new Rectangle[numberOfRoofs];
    private float[] roofX = new float[numberOfRoofs];
    private float[] roofY = new float[numberOfRoofs];
    private float[] foodX = new float[numberOfRoofs];
    private float[] foodY = new float[numberOfRoofs];
    private float roofOffsetVelocityX = 5;

    boolean gameIsRunning;
    float descentVelocity;
    private float jumpSize = 40f;
    private int distanceBetweenRoof = 1200;
    private int distanceBetweenFood = 900;
    private int jumpDecrease = 2; //снижение способности прыгать когда ешь еду


    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();

        batch = new SpriteBatch();
        background = new Texture("bg.png");

        for (int i = 0; i < numberOfRoofs; i++) {
            roof[i] = new Texture("roof.png");
            roofX[i] = i * distanceBetweenRoof;
            roofY[i] = new Random(System.currentTimeMillis()).nextInt(Gdx.graphics.getHeight() / 5);
            roofRectangle[i] = new Rectangle();

        }

        for (int i = 0; i < numberOfFood; i++) {
            food[i] = new Texture("food.png");
            foodX[i] = i * distanceBetweenFood;
            foodY[i] = getRandomFoodY();
            foodCircle[i] = new Circle();
            foodCollision[i] = false;
        }


        bird[0] = new Texture("bird.png");
        bird[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;
        birdX = Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2;


    }

    private int getRandomRoofY() {
        float minY = 0;
        int dispersionY = Gdx.graphics.getHeight() / 2;
        return new Random(System.currentTimeMillis()).nextInt(dispersionY);
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
                        break;
                    }
                }
            }


            //меняем состояние птицы чтобы крыльями махала
            if (birdState == 0) {
                birdState = 1;
            } else {
                birdState = 0;
            }


            //крыши бегут непрерывно
            for (int i = 0; i < numberOfRoofs; i++) {
                roofX[i] -= roofOffsetVelocityX;
//
                //если крыша ушла за экран то вместо нее рисуем новую после последней крыши
                if (roofX[i] + roof[i].getWidth() < 0) {
                    roofX[i] = roofX[numberOfRoofs - 1] + (i + 1) * distanceBetweenRoof;
                    roofY[i] = getRandomRoofY();

                }

            }

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

            Gdx.app.log("Food", "" + jumpSize);

        } else {
            if (Gdx.input.justTouched()) {
                gameIsRunning = true;

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
            if(!foodCollision[i]) {
                batch.draw(food[i], foodX[i], foodY[i], food[i].getWidth(), food[i].getHeight());
            }
            foodCircle[i].set(foodX[i] + food[i].getWidth() / 2,
                        foodY[i] + food[i].getHeight() / 2,
                        food[i].getHeight() / 2);

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
