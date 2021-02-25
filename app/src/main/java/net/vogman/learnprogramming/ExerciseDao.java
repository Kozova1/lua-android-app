package net.vogman.learnprogramming;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
  @Query("SELECT * FROM `Exercise`")
  LiveData<List<Exercise>> loadExcercises();

  @Query("UPDATE `Exercise` SET IsDone = :val, Template=:solution WHERE uid = :id")
  void markAsDone(int id, boolean val, String solution);

  @Insert
  void addExercise(Exercise exercise);
}
