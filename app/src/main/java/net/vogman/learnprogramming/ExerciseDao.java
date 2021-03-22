package net.vogman.learnprogramming;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
  @Query("SELECT * FROM `Exercise`")
  LiveData<List<Exercise>> loadExercises();

  @Query("UPDATE `Exercise` SET IsDone = :val, Template=:solution WHERE uid = :id")
  void markAsDone(int id, boolean val, String solution);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void addExercise(Exercise exercise);

  @Query("DELETE FROM `Exercise` WHERE uid = :id")
  void remove(int id);

  @Query("DELETE FROM `Exercise`")
  void clear();

  @Insert
  void insertAll(List<Exercise> exercises);
}
