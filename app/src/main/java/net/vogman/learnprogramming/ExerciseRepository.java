package net.vogman.learnprogramming;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseRepository {
  private final LiveData<List<Exercise>> exercises;
  private final ExerciseDao dao;

  ExerciseRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    dao = db.exerciseDao();
    exercises = dao.loadExcercises();
  }

  LiveData<List<Exercise>> getExercises() {
    return exercises;
  }

  void markAsDone(int id, boolean val, String solution) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      dao.markAsDone(id, val, solution);
    });
  }

  void addExercise(Exercise exercise) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      dao.addExercise(exercise);
    });
  }
}
