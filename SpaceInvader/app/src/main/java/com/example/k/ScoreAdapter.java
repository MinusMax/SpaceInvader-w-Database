package com.example.k;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<Score> scores = new ArrayList<>();

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score currentScore = scores.get(position);
        holder.scoreTextView.setText(String.valueOf(currentScore.getScore())); // Assuming getScore() is a method in your Score class
    }

    @Override
    public int getItemCount() {
        return scores.size(); // Return the size of the scores list
    }

    public void setScores(List<Score> scores) {
        this.scores = scores; // Update the scores list
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView scoreTextView;

        ScoreViewHolder(View itemView) {
            super(itemView);
            scoreTextView = itemView.findViewById(R.id.score_text_view); // Your TextView for the score
        }
    }
}
