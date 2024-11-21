package com.example.k;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score); // Method to insert a score

    @Query("SELECT * FROM score_table ORDER BY score DESC")
    LiveData<List<Score>> getAllScores(); // Method to get all scores

    @Query("DELETE FROM score_table") // Method to delete all scores
    void deleteAllScores();
}
