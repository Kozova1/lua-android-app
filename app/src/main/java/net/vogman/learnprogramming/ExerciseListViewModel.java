package net.vogman.learnprogramming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseListViewModel extends AndroidViewModel {
  private final LiveData<List<Exercise>> exercises;

  public ExerciseListViewModel(Application application) {
    super(application);
    ExerciseRepository repo = new ExerciseRepository(application);
    exercises = repo.getExercises();
  }

  public LiveData<List<Exercise>> getExercises() {
    return exercises;
  }
}
