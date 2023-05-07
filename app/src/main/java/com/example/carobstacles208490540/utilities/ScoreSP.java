package com.example.carobstacles208490540.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.carobstacles208490540.RowScoreTable;
import com.google.gson.Gson;

import java.util.Arrays;


public class ScoreSP {
    private static final String DB_FILE = "DB_FILE";
    public static final String JSON_SCORES_KEY = "JSON_SCORES_KEY";

    public static RowScoreTable getScore(Context context, int idx) {
        RowScoreTable[] scores = getScores(context);
        return scores[idx];
    }

    public static void putScore(Context context, RowScoreTable scoreItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        RowScoreTable[] scoresFromJson = getScores(context);
        RowScoreTable[] newScores = Arrays.copyOf(scoresFromJson, scoresFromJson.length + 1);
        newScores[newScores.length - 1] = scoreItem;

        Gson gson = new Gson();
        String updatedJsonScores = gson.toJson(newScores);
        editor.putString(JSON_SCORES_KEY, updatedJsonScores);
        editor.apply();
    }

    public static RowScoreTable[] getScores(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        String scoresJson = sharedPreferences.getString(JSON_SCORES_KEY, "[]");
        Gson gson = new Gson();
        RowScoreTable[] scores = gson.fromJson(scoresJson, RowScoreTable[].class);

        return scores;
    }
}
