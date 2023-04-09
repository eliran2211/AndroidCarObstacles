package com.example.carobstacles208490540;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;

public class GameControl {
    public boolean[][] obstacleVisibility;
    public boolean[] carVisibility;

    private int carColIndex;
    private int rows;
    private int cols;
    private int lives;
    private boolean isFinished;


    public GameControl() {}
    public GameControl(int rows, int cols, int carColIndex){
        this.rows = rows;
        this.cols = cols;
        this.obstacleVisibility = new boolean[rows][cols];
        this.carColIndex = carColIndex;
        this.lives = 3;
        isFinished = false;
        carVisibility = new boolean[3];
        carVisibility[carColIndex] = true;
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

    public boolean getIsFinished() {
        return this.isFinished;
    }
    public void setObstaclesVisibility(int row, int col, boolean isVisible) {
        this.obstacleVisibility[row][col] = isVisible;
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

    public void nextTick() {
        boolean[][] updatedObstacleVisibility = new boolean[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                boolean currentObstacle = this.obstacleVisibility[i][j];
                if(currentObstacle && i + 1 < this.rows) {
                    updatedObstacleVisibility[i + 1][j] = true;
                }else if( updatedObstacleVisibility[i][j] && i + 1 == this.rows && this.carColIndex == j){
                        this.lives--;
                        updatedObstacleVisibility[i][j] = false;
                        if(this.lives <= 0){
                            this.isFinished = true;
                        }
                    }
            }
        }

        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
               this.obstacleVisibility[i][j] = updatedObstacleVisibility[i][j];
            }
        }
    }
}
