package com.example.carobstacles208490540.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carobstacles208490540.Interfaces.CallBack_sendScoreItemClick;
import com.example.carobstacles208490540.R;
import com.example.carobstacles208490540.RowScoreTable;

public class ScoreItemAdapter extends RecyclerView.Adapter<ScoreItemAdapter.ScoreItemViewHolder>{

    private Context context;
    private RowScoreTable[] scores;
    private CallBack_sendScoreItemClick listener;

    public ScoreItemAdapter(Context context, RowScoreTable[] scores, CallBack_sendScoreItemClick listener) {
        this.context = context;
        this.scores = scores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        ScoreItemViewHolder scoreItemViewHolder = new ScoreItemViewHolder(view);
        return scoreItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreItemViewHolder holder, int position) {
        RowScoreTable score = getItem(position);
        holder.scoreItem_LBL_serialNumber.setText((position+1) + "");
        holder.scoreItem_LBL_name.setText(score.getName());
        holder.scoreItem_LBL_scoreNumber.setText(score.getScore() + "");
        holder.scoreItem_LAYOUT_scoreWrapper.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.scoreItemClick(score.getLongitude(), score.getLatitude());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.scores == null ? 0 : scores.length;
    }

    private RowScoreTable getItem(int position) {
        return this.scores[position];
    }

    public class ScoreItemViewHolder extends RecyclerView.ViewHolder {
        private TextView scoreItem_LBL_serialNumber;
        private TextView scoreItem_LBL_name;
        private TextView scoreItem_LBL_scoreNumber;
        private LinearLayout scoreItem_LAYOUT_scoreWrapper;

        public ScoreItemViewHolder(@NonNull View itemView) {
            super(itemView);
            scoreItem_LBL_serialNumber = itemView.findViewById(R.id.scoreItem_LBL_serialNumber);
            scoreItem_LBL_name = itemView.findViewById(R.id.scoreItem_LBL_name);
            scoreItem_LBL_scoreNumber = itemView.findViewById(R.id.scoreItem_LBL_scoreNumber);
            scoreItem_LAYOUT_scoreWrapper = itemView.findViewById(R.id.scoreItem_LAYOUT_scoreWrapper);
        }
    }
}
