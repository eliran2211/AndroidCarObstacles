package com.example.carobstacles208490540;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private int DELAY = 1000;
    private boolean blockGenerateNumber = false;
    private ArrayList<ShapeableImageView> main_IMG_hearts;
    private ShapeableImageView[] main_IMG_car;
    private AppCompatImageView main_IMG_background;

    private LinearLayout linearLayoutCar;
    private LinearLayout linearLayoutStones;
    private MaterialButton main_BTN_arrowRight;
    private MaterialButton main_BTN_arrowLeft;
    private int lives = 3;
    private GameControl gameControl;
    private final Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!gameControl.getIsFinished() && gameControl.getLives() != 0) {
                if (!blockGenerateNumber) {
                    int randomColumn = GameControl.generateRandomNumber(3);
                    gameControl.setObstaclesVisibility(0, randomColumn, true);
                    blockGenerateNumber = true;
                } else {
                    blockGenerateNumber = false;
                }
                handler.postDelayed(this, DELAY); //Do it again in a second
                gameControl.nextTick();
                refreshGameBoard();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addViews();
        addListeners();
        drawGridStones();

        gameControl = new GameControl(7, 3, 1);
        startGame();
    }

    private void addViews() {
        main_IMG_hearts = new ArrayList<>();
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart1));
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart2));
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart3));

        main_IMG_car = new ShapeableImageView[3];
        main_IMG_car[0] = findViewById(R.id.main_IMG_car1);
        main_IMG_car[1] = findViewById(R.id.main_IMG_car2);
        main_IMG_car[2] = findViewById(R.id.main_IMG_car3);
        linearLayoutCar = findViewById(R.id.main_LAYOUT_car);
        linearLayoutStones = findViewById(R.id.main_LAYOUT_stones);
        main_BTN_arrowLeft = findViewById(R.id.main_FAB_arrowLeft);
        main_BTN_arrowRight = findViewById(R.id.main_FAB_arrowRight);
    }
    private void addListeners() {
        main_BTN_arrowLeft.setOnClickListener(v -> {
            gameControl.moveCarLeft();
            refreshGameBoard();
        });
        main_BTN_arrowRight.setOnClickListener(v -> {
            gameControl.moveCarRight();
            refreshGameBoard();
        });
    }

    private LinearLayout getRowLayoutStones(int layoutId) {
        LinearLayout linearLayoutRowStones = new LinearLayout(this);
        linearLayoutRowStones.setOrientation(LinearLayout.VERTICAL);
        linearLayoutRowStones.setId(layoutId);

        linearLayoutRowStones.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayoutRowStones;
    }

    private ShapeableImageView getStoneImg(int stoneViewId){
        ShapeableImageView stoneImageView = new ShapeableImageView(this);
        stoneImageView.setId(stoneViewId);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 280, 1f);
        stoneImageView.setLayoutParams(layoutParams);

        stoneImageView.setImageResource(R.drawable.ic_icon_stone);
        return stoneImageView;
    }

    private void drawGridStones() {
        for(int i = 0; i < 7; i++){
            int layoutId = ("main_LAYOUT_stones" + i).hashCode();
            LinearLayout linearLayoutRowStones = getRowLayoutStones(layoutId);
            for(int j = 0; j < 3; j++){
                int stoneId = ("main_IMG_stone" + + i + j).hashCode();
                ShapeableImageView stoneImg = getStoneImg(stoneId);
                stoneImg.setVisibility(View.INVISIBLE);
                linearLayoutRowStones.addView(stoneImg);
            }
            linearLayoutStones.addView(linearLayoutRowStones);

        }
    }

    private void setVisibleStoneImage(int row, int col, int visible) {
        LinearLayout rowLinearLayout = (LinearLayout) linearLayoutStones.getChildAt(row);
        ShapeableImageView stoneImg = (ShapeableImageView) rowLinearLayout.getChildAt(col);
        stoneImg.setVisibility(visible);
    }

    private void setInvisibleLivesImg(int countInvisibleLives){
        if(this.lives != 0 && this.lives != this.gameControl.getLives()){
            this.lives--;
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            Toast.makeText(this,"oops :(", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        }
        for(int i = 0; i < countInvisibleLives; i++){
            main_IMG_hearts.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void startGame() {
        handler.postDelayed(runnable,0);
    }

    private void refreshGameBoard(){
        setInvisibleLivesImg(3 - gameControl.getLives());
        for(int i = 0; i < gameControl.carVisibility.length; i++){
            linearLayoutCar.getChildAt(i).setVisibility(gameControl.carVisibility[i] ? View.VISIBLE : View.INVISIBLE);
        }
        for (int i = 0; i < gameControl.getRows(); i++){
            for(int j = 0; j < gameControl.getCols(); j++){
                if(gameControl.obstacleVisibility[i][j]){
                    setVisibleStoneImage(i, j, View.VISIBLE);
                }else{
                    setVisibleStoneImage(i, j, View.INVISIBLE);
                }
            }
        }
    }
}