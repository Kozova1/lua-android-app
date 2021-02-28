package net.vogman.learnprogramming;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.jetbrains.annotations.NotNull;

public class ExerciseListAdapter extends ListAdapter<Exercise, Listing<Exercise>> {
  public ExerciseListAdapter(@NotNull DiffUtil.ItemCallback<Exercise> diffCallback) {
    super(diffCallback);
  }

  @Override
  public void onBindViewHolder(@NonNull Listing<Exercise> holder, int position) {
    Exercise current = getItem(position);
    holder.bindTo(current);
  }

  @NonNull
  @Override
  public Listing<Exercise> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return Listing.createExercise(parent);
  }

  static class ExerciseDiff extends DiffUtil.ItemCallback<Exercise> {
    @Override
    public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
      return oldItem.title.equals(newItem.title);
    }
  }
}
