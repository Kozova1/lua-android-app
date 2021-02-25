package net.vogman.learnprogramming;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.jetbrains.annotations.NotNull;

public class ExerciseListAdapter extends ListAdapter<Exercise, ExerciseListing>  {
  public ExerciseListAdapter(@NotNull DiffUtil.ItemCallback<Exercise> diffCallback) {
    super(diffCallback);
  }

  @Override
  public void onBindViewHolder(@NonNull ExerciseListing holder, int position) {
    Exercise current = getItem(position);
    holder.title(current.title);
    holder.done(current.isDone);
    holder.hard(current.isHard);
    holder.instructions(current.instructions);
    holder.template(current.template);
    holder.id(current.uid);
    holder.test(current.test);
  }

  @NonNull
  @Override
  public ExerciseListing onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ExerciseListing.create(parent);
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
