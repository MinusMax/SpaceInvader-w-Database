package com.example.k;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreDisplayActivity extends AppCompatActivity {
    private ScoreViewModel scoreViewModel;
    private ScoreAdapter adapter; // Assuming you have a ScoreAdapter to bind scores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_display); // Set the content view to your layout

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter(); // Initialize your adapter
        recyclerView.setAdapter(adapter);

        // Initialize the ScoreViewModel
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        // Set up the observer
        scoreViewModel.getAllScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {
                adapter.setScores(scores);  // Update the RecyclerView adapter
            }
        });

        // Setup the delete button
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreViewModel.deleteAllScores(); // Call to delete all scores
            }
        });
    }
}
