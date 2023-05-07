package com.example.carobstacles208490540;

import android.location.Location;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameControl {
    private float[] collisions = {-8.8f, -4.9f, 0, 4.9f, 8.8f};
    private int COIN_SCORE = 5;
    public boolean[][] obstacleVisibility;
    public boolean[][] coinsVisibility;
    public boolean[] carVisibility;

    private int carColIndex;
    private int rows;
    private int cols;
    private int lives;
    private boolean isFinished;
    private int score;
    private RowScoreTable finalScore;
    private boolean isSensorMode;
    private float carWidth;
    private MediaPlayer coinMP;

    public GameControl(int rows, int cols, int carColIndex, String name, Location location, boolean isSensorMode, float carWidth, MediaPlayer coinMP){
        this.rows = rows;
        this.cols = cols;
        this.obstacleVisibility = new boolean[rows][cols];
        this.coinsVisibility = new boolean[rows][cols];
        this.carColIndex = carColIndex;
        this.lives = 3;
        isFinished = true;
        carVisibility = new boolean[5];
        carVisibility[carColIndex] = true;
        this.score = 0;
        this.finalScore = new RowScoreTable();
        finalScore.setName(name);
        finalScore.setLatitude(location.getLatitude());
        finalScore.setLongitude(location.getLongitude());
        this.isSensorMode = isSensorMode;
        this.carWidth = carWidth;
        this.coinMP = coinMP;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getLives() {
        return lives;
    }

    public RowScoreTable getFinalScore() { return finalScore; }
    public int getScore() { return score; }
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }
    public void setObstaclesVisibility(int row, int col, boolean isVisible) {
        this.obstacleVisibility[row][col] = isVisible;
    }
    public void setCoinsVisibility(int row, int col, boolean isVisible) {
        this.coinsVisibility[row][col] = isVisible;
    }


    public static int generateRandomNumber(int max){
        Random random = new Random();
        return random.nextInt(max);
    }

    public void moveCarLeft() {
        if(this.carColIndex - 1 < 0){
            return;
        }

        this.carVisibility[this.carColIndex] = false;
        this.carVisibility[--this.carColIndex] = true;
    }

    public void moveCarRight() {
        if(this.carColIndex + 1 == this.carVisibility.length){
            return;
        }

        this.carVisibility[this.carColIndex] = false;
        this.carVisibility[++this.carColIndex] = true;
    }

    public void nextTick(float xCoordCar) {
        boolean[][] updatedObstacleVisibility = new boolean[this.rows][this.cols];
        boolean[][] updatedCoinsVisibility = new boolean[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                boolean currentObstacle = this.obstacleVisibility[i][j];
                boolean currentCoin = this.coinsVisibility[i][j];
                //obstacle
                if(currentObstacle && i + 1 < this.rows) {
                    updatedObstacleVisibility[i + 1][j] = true;
                }else if( updatedObstacleVisibility[i][j] && i + 1 == this.rows){
                        if(!this.isSensorMode && this.carColIndex == j){
                            this.lives--;
                        }else if(isSensorMode){
                            if(xCoordCar >= (collisions[j] - (this.carWidth/2)) && xCoordCar <= collisions[j] + (this.carWidth/2)){
                               this.lives--;
                            }
                        }
                        updatedObstacleVisibility[i][j] = false;
                        if(this.lives <= 0){
                            this.isFinished = true;
                            return;
                        }
                }

                if(currentCoin && i + 1 < this.rows) {
                    updatedCoinsVisibility[i + 1][j] = true;
                }else if(updatedCoinsVisibility[i][j] && i + 1 == this.rows){
                    if(!isSensorMode && this.carColIndex == j){
                        this.score += COIN_SCORE;
                        coinMP.start();
                    }else if(isSensorMode){
                        if(xCoordCar >= (collisions[j] - (this.carWidth/2)) && xCoordCar <= collisions[j] + (this.carWidth/2)){
                            this.score += COIN_SCORE;
                            coinMP.start();
                        }
                    }
                    updatedCoinsVisibility[i][j] = false;
                    this.finalScore.setScore(this.score);
                }
            }
        }

        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
               this.obstacleVisibility[i][j] = updatedObstacleVisibility[i][j];
               this.coinsVisibility[i][j] = updatedCoinsVisibility[i][j];
            }
        }
    }

    public void resetGame() {
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                this.obstacleVisibility[i][j] = false;
                this.coinsVisibility[i][j] = false;
            }
        }
        this.score = 0;
        this.lives = 3;
        this.isFinished = false;
    }
}
