package com.example.carobstacles208490540;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;

import android.os.Bundle;

import com.example.carobstacles208490540.Interfaces.StepCallback;
import com.example.carobstacles208490540.utilities.CarStepDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final String KEY_DELAY = "KEY_DELAY";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_SENSOR_MODE = "KEY_SENSOR_MODE";
    public static final String KEY_LOCATION = "KEY_LOCATION";
    private int delay;
    private boolean blockGenerateNumberStone = false;
    private int reGenerateNumberCoin = 4; //WAIT 4 TICKS BEFORE REGENERATE
    private ArrayList<ShapeableImageView> main_IMG_hearts;
    private ShapeableImageView[] main_IMG_car;
    private ImageView main_IMG_carSensor;

    private LinearLayout linearLayoutCar;
    private LinearLayout linearLayoutStones;
    private MaterialButton main_BTN_arrowRight;
    private MaterialButton main_BTN_arrowLeft;
    private int lives = 3;
    private GameControl gameControl;
    private final Handler handler = new Handler();
    private TextView main_TXT_score;
    private String name;
    private boolean isSensorMode;
    private TextView main_TXT_distance;
    private int odometer = 0;

    private CarStepDetector stepDetector;

    private MediaPlayer coinMP;
    private float xCoordCar = 0;
    private Location userLocation;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(gameControl.getLives() > 0) {
                if (!blockGenerateNumberStone) {
                    int randomStoneColumn = GameControl.generateRandomNumber(5);
                    gameControl.setObstaclesVisibility(0, randomStoneColumn, true);
                    blockGenerateNumberStone = true;
                } else {
                    blockGenerateNumberStone = false;
                }

                if(reGenerateNumberCoin <= 0){
                    reGenerateNumberCoin = 2;

                    int randomCoinColumn;
                    do{
                        randomCoinColumn = GameControl.generateRandomNumber(5);
                    }while(gameControl.obstacleVisibility[0][randomCoinColumn]);

                    gameControl.setCoinsVisibility(0, randomCoinColumn, true);
                }else{
                    reGenerateNumberCoin--;
                }

                handler.postDelayed(this, delay); //Do it again in a second
                gameControl.nextTick(xCoordCar);
                refreshGameBoard();
            }else {
                openScoreActivity();
            }
        }
    };

    private void openScoreActivity() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra(ScoreActivity.KEY_SCORES, gameControl.getFinalScore());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent previousIntent = getIntent();
        delay = previousIntent.getIntExtra(KEY_DELAY, 1000);
        name = previousIntent.getStringExtra(KEY_NAME);
        userLocation = previousIntent.getParcelableExtra(KEY_LOCATION);

        isSensorMode = previousIntent.getBooleanExtra(KEY_SENSOR_MODE, false);

        addViews();
        addListeners();
        drawGridStones();

        if(isSensorMode){
            stepDetector = new CarStepDetector(this, getResources().getDisplayMetrics().widthPixels, 10f, new StepCallback() {
                @Override
                public void stepX() {
                    xCoordCar = stepDetector.getStepX();
                    main_IMG_carSensor.setX(xCoordCar*45);
                }
            });
        }
        coinMP = MediaPlayer.create(this, R.raw.pick_up_coin);
        coinMP.setVolume(1.0f,1.0f);
        gameControl = new GameControl(7, 5, 1, name, userLocation, isSensorMode, 3.9f, coinMP);
        startGame();
    }
    protected void onResume() {
        super.onResume();

//        coinMP.start();
        if(isSensorMode){
            stepDetector.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        mediaPlayer.stop();
        if(isSensorMode){
            stepDetector.stop();
        }
    }

    private void addViews() {
        main_IMG_hearts = new ArrayList<>();
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart1));
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart2));
        main_IMG_hearts.add(findViewById(R.id.main_IMG_heart3));
        main_IMG_carSensor = findViewById(R.id.main_IMG_carSensor);
        main_IMG_car = new ShapeableImageView[5];
        main_IMG_car[0] = findViewById(R.id.main_IMG_car1);
        main_IMG_car[1] = findViewById(R.id.main_IMG_car2);
        main_IMG_car[2] = findViewById(R.id.main_IMG_car3);
        main_IMG_car[3] = findViewById(R.id.main_IMG_car3);
        main_IMG_car[4] = findViewById(R.id.main_IMG_car5);
        linearLayoutCar = findViewById(R.id.main_LAYOUT_car);
        main_TXT_distance = findViewById(R.id.main_TXT_distance);

        if(!isSensorMode){
            main_IMG_carSensor.setVisibility(View.GONE);
            linearLayoutCar.getChildAt(5).setVisibility(View.GONE);
        }else {
            linearLayoutCar.getChildAt(0).setVisibility(View.GONE);
            linearLayoutCar.getChildAt(1).setVisibility(View.GONE);
            linearLayoutCar.getChildAt(2).setVisibility(View.GONE);
            linearLayoutCar.getChildAt(3).setVisibility(View.GONE);
            linearLayoutCar.getChildAt(4).setVisibility(View.GONE);
        }

        linearLayoutStones = findViewById(R.id.main_LAYOUT_board);
        main_BTN_arrowLeft = findViewById(R.id.main_FAB_arrowLeft);
        main_BTN_arrowRight = findViewById(R.id.main_FAB_arrowRight);
       if(isSensorMode) {
           main_BTN_arrowLeft.setVisibility(View.GONE);
           main_BTN_arrowRight.setVisibility(View.GONE);
           removeBoardMarginSides();
       }
        main_TXT_score = findViewById(R.id.main_TXT_score);
    }
    private void addListeners() {
        if(!isSensorMode){
            main_BTN_arrowLeft.setOnClickListener(v -> {
                gameControl.moveCarLeft();
                refreshGameBoard();
            });
            main_BTN_arrowRight.setOnClickListener(v -> {
                gameControl.moveCarRight();
                refreshGameBoard();
            });
        }
    }

    private void removeBoardMarginSides() {
        ViewGroup.LayoutParams params = linearLayoutStones.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
        marginParams.setMarginStart(0);
        marginParams.setMarginEnd(0);
        marginParams.setMargins(0,0,0,0);
        linearLayoutStones.setLayoutParams(marginParams);

        params = linearLayoutCar.getLayoutParams();
        marginParams = (ViewGroup.MarginLayoutParams) params;
        marginParams.setMarginStart(0);
        marginParams.setMarginEnd(0);
        marginParams.setMargins(0,0,0,0);
        linearLayoutCar.setLayoutParams(marginParams);
    }

    private LinearLayout getRowLayoutStones(int layoutId) {
        LinearLayout linearLayoutRowStones = new LinearLayout(this);
        linearLayoutRowStones.setId(layoutId);

        linearLayoutRowStones.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayoutRowStones;
    }

    private FrameLayout getFrameLayoutImages(){
        FrameLayout fl = new FrameLayout(this);
        fl.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.weight = 1f;
        fl.setLayoutParams(params);

        return fl;
    }

    private ShapeableImageView getStoneImg(int stoneViewId){
        ShapeableImageView stoneImageView = new ShapeableImageView(this);
        stoneImageView.setId(stoneViewId);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 280, 1f);
        stoneImageView.setLayoutParams(layoutParams);

        stoneImageView.setImageResource(R.drawable.ic_icon_stone);
        return stoneImageView;
    }
    private ShapeableImageView getCoinImg(int coinViewId){
        ShapeableImageView coinImageView = new ShapeableImageView(this);
        coinImageView.setId(coinViewId);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 280, 1f);
        coinImageView.setLayoutParams(layoutParams);

        coinImageView.setImageResource(R.drawable.ic_icon_dollar);
        return coinImageView;
    }

    private void drawGridStones() {
        for(int i = 0; i < 7; i++){
            int layoutId = ("main_LAYOUT_stones" + i).hashCode();
            LinearLayout linearLayoutRowStones = getRowLayoutStones(layoutId);
            for(int j = 0; j < 5; j++){
                FrameLayout imagesFrameLayout = getFrameLayoutImages();
                int stoneId = ("main_IMG_stone" + + i + j).hashCode();
                ShapeableImageView stoneImg = getStoneImg(stoneId);
                ShapeableImageView coinImg = getCoinImg(stoneId);

                stoneImg.setVisibility(View.INVISIBLE);
                coinImg.setVisibility(View.INVISIBLE);

                imagesFrameLayout.addView(stoneImg);
                imagesFrameLayout.addView(coinImg);

                linearLayoutRowStones.addView(imagesFrameLayout);
            }
            linearLayoutStones.addView(linearLayoutRowStones);

        }
    }

    private void setVisibleStoneImage(int row, int col, int visible) {
        LinearLayout rowLinearLayout = (LinearLayout) linearLayoutStones.getChildAt(row);
        FrameLayout columnFrameLayout = (FrameLayout) rowLinearLayout.getChildAt(col);
        ShapeableImageView stoneImg = (ShapeableImageView) columnFrameLayout.getChildAt(0);
        stoneImg.setVisibility(visible);
    }

    private void setVisibleCoinImage(int row, int col, int visible) {
        LinearLayout rowLinearLayout = (LinearLayout) linearLayoutStones.getChildAt(row);
        FrameLayout columnFrameLayout = (FrameLayout) rowLinearLayout.getChildAt(col);
        ShapeableImageView coinImg = (ShapeableImageView) columnFrameLayout.getChildAt(1);
        coinImg.setVisibility(visible);
    }
    private void visibleLives(int countToVisible, int toVisible) {
        for(int i = 0; i < countToVisible; i++){
            main_IMG_hearts.get(i).setVisibility(toVisible);
        }
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
        visibleLives(countInvisibleLives, View.INVISIBLE);
    }

    private void startGame() {
        handler.postDelayed(runnable,0);
    }
    private void setScoreView(int score) {
        main_TXT_score.setText("Score: " + score);
    }
    private void setDistance(){
        odometer += 5;
        main_TXT_distance.setText("Distance: " + odometer);
    }
    private void refreshGameBoard(){
        setInvisibleLivesImg(3 - gameControl.getLives());
        setScoreView(gameControl.getScore());
        setDistance();
        for(int i = 0; i < gameControl.carVisibility.length; i++){
            if(!isSensorMode){
                linearLayoutCar.getChildAt(i).setVisibility(gameControl.carVisibility[i] ? View.VISIBLE : View.INVISIBLE);
            }
        }
        for (int i = 0; i < gameControl.getRows(); i++){
            for(int j = 0; j < gameControl.getCols(); j++){
                if(gameControl.obstacleVisibility[i][j]){
                    setVisibleStoneImage(i, j, View.VISIBLE);
                }else{
                    setVisibleStoneImage(i, j, View.INVISIBLE);
                }

                if(gameControl.coinsVisibility[i][j]){
                    setVisibleCoinImage(i, j, View.VISIBLE);
                }else{
                    setVisibleCoinImage(i, j, View.INVISIBLE);
               }
            }
        }
    }
}