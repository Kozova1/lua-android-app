package net.vogman.learnprogramming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseListFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

    RecyclerView recycler = view.findViewById(R.id.exercisesRecyclerView);
    final ExerciseListAdapter adapter = new ExerciseListAdapter(new ExerciseListAdapter.ExerciseDiff());

    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    recycler.setAdapter(adapter);

    ExerciseListViewModel viewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);
    viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
      exercises.sort((article1, article2) -> article1.uid - article2.uid);
      adapter.submitList(exercises);
    });

    return view;
  }
}