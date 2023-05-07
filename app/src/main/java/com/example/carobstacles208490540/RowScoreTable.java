package com.example.carobstacles208490540;

import java.io.Serializable;

public class RowScoreTable implements Serializable, Comparable<RowScoreTable>{
    private int score;
    private String name;
    private double latitude;
    private double longitude;

    public RowScoreTable(){

    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(RowScoreTable rowScoreTable) {
        if(rowScoreTable.getScore() == this.getScore()){
            return 0;
        }else if(rowScoreTable.getScore() > this.getScore()){
            return 1;
        }else {
            return -1;
        }
    }
}
