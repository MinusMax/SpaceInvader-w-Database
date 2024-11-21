package com.example.k;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider; // Ensure this is imported
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private ScoreViewModel scoreViewModel; // Declare ScoreViewModel
    private RecyclerView recyclerView; // RecyclerView to display the scores
    private ScoreAdapter adapter; // Adapter for the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score); // Ensure this matches your layout file name

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreAdapter(); // Initialize your adapter
        recyclerView.setAdapter(adapter);

        // Initialize the ScoreViewModel
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {
                adapter.setScores(scores);  // Update the RecyclerView adapter
            }
        });
    }
}
