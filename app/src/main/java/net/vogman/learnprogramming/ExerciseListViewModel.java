package net.vogman.learnprogramming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseListViewModel extends AndroidViewModel {
  private final LiveData<List<Exercise>> exercises;
  private final ExerciseRepository repo;

  public ExerciseListViewModel(Application application) {
    super(application);
    repo = new ExerciseRepository(application);
    exercises = repo.getExercises();
  }

  public LiveData<List<Exercise>> getExercises() {
    return exercises;
  }

  public void markAsDone(int id, String s) {
    repo.markAsDone(id, true, s);
  }
}
