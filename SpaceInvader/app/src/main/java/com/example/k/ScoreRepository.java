package com.example.k;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {
    private ScoreDao scoreDao; // DAO for accessing the database
    private LiveData<List<Score>> allScores; // LiveData for observing scores

    public ScoreRepository(Application application) {
        ScoreRoomDatabase db = ScoreRoomDatabase.getDatabase(application); // Get instance of the Room database
        scoreDao = db.scoreDao(); // Initialize DAO
        allScores = scoreDao.getAllScores(); // Get all scores from the DAO
    }

    public LiveData<List<Score>> getAllScores() {
        return allScores; // Return LiveData for observation
    }

    public void insert(Score score) {
        ScoreRoomDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.insert(score); // Insert the score into the database
        });
    }

    // Implement the deleteAllScores method
    public void deleteAllScores() {
        ScoreRoomDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.deleteAllScores(); // Call the delete method from the DAO
        });
    }
}
