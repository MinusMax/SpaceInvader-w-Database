package com.example.k;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {
    private ScoreRepository repository;
    private LiveData<List<Score>> allScores;

    public ScoreViewModel(Application application) {
        super(application);
        repository = new ScoreRepository(application);
        allScores = repository.getAllScores();
    }

    public LiveData<List<Score>> getAllScores() {
        return allScores; // Return LiveData
    }

    public void insert(Score score) {
        repository.insert(score);
    }

    public void deleteAllScores() {
        repository.deleteAllScores();
    }
}
