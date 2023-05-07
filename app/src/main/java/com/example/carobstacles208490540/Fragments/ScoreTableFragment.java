package com.example.carobstacles208490540.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carobstacles208490540.Adapters.ScoreItemAdapter;
import com.example.carobstacles208490540.Interfaces.CallBack_sendCoords;
import com.example.carobstacles208490540.Interfaces.CallBack_sendScoreItemClick;
import com.example.carobstacles208490540.R;
import com.example.carobstacles208490540.RowScoreTable;

public class ScoreTableFragment extends Fragment {

    private RowScoreTable[] scoresToPrint;
    private RecyclerView score_LST_scores;
    CallBack_sendCoords sendCoords;

    public ScoreTableFragment(RowScoreTable[] scoresToPrint, CallBack_sendCoords sendCoords) {
        this.scoresToPrint = scoresToPrint;
        this.sendCoords = sendCoords;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_table, container, false);
        score_LST_scores = view.findViewById(R.id.score_LST_scores);

        ScoreItemAdapter scoreAdapter = new ScoreItemAdapter(getActivity(), scoresToPrint, clickScoreItemCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        score_LST_scores.setLayoutManager(linearLayoutManager);
        score_LST_scores.setAdapter(scoreAdapter);
        return view;
    }

    CallBack_sendScoreItemClick clickScoreItemCallback = new CallBack_sendScoreItemClick() {
        @Override
        public void scoreItemClick(double longitude, double latitude) {
            sendCoords.sendLangAndLong(longitude, latitude);
        }
    };
}