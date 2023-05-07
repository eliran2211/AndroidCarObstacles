package com.example.carobstacles208490540;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.carobstacles208490540.Fragments.MapFragment;
import com.example.carobstacles208490540.Fragments.ScoreTableFragment;
import com.example.carobstacles208490540.Interfaces.CallBack_sendCoords;
import com.example.carobstacles208490540.utilities.ScoreSP;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Arrays;

public class ScoreActivity extends AppCompatActivity {

    public static String KEY_SCORES = "KEY_SCORES";
    private MaterialButton score_FAB_playAgain;
    private FrameLayout score_FRAME_map;
    private MapFragment mapFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent previousIntent = getIntent();
        RowScoreTable currentScore = (RowScoreTable) previousIntent.getSerializableExtra(KEY_SCORES);
        ScoreSP.putScore(this,currentScore);// put new score
        RowScoreTable[] scoresToPrint = ScoreSP.getScores(this); //get all scores from JSON
        Arrays.sort(scoresToPrint);
        addViews();
        addListeners();
        initMapFragment();
        initScoreFragment(scoresToPrint);
    }
    private void addViews() {
        score_FAB_playAgain = findViewById(R.id.score_FAB_playAgain);
        score_FRAME_map = findViewById(R.id.score_FRAME_map);
    }

    private void initMapFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mapFragment = new MapFragment();
        transaction.replace(R.id.score_FRAME_map, mapFragment);
        transaction.commitNow();
    }

    private void initScoreFragment(RowScoreTable[] scoresToPrint){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.score_FRAME_tableScore, new ScoreTableFragment(scoresToPrint, sendCoords));
        transaction.commitNow();
    }
    CallBack_sendCoords sendCoords = new CallBack_sendCoords() {
        @Override
        public void sendLangAndLong(double longitude, double latitude) {
            try {
                mapFragment.moveCameraAndSetMarker(longitude, latitude);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private void addListeners() {
        score_FAB_playAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });
    }
}
