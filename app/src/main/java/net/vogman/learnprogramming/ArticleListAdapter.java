package net.vogman.learnprogramming;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArticleListAdapter extends ListAdapter<Article, ArticleListing> {
  public ArticleListAdapter(@NotNull DiffUtil.ItemCallback<Article> diffCallback) {
    super(diffCallback);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleListing holder, int position) {
    Article current = getItem(position);
    holder.title(current.title);
    holder.read(current.hasBeenRead);
    holder.hard(current.isHard);
    holder.contents(current.contents);
    holder.id(current.uid);
  }

  @NonNull
  @Override
  public ArticleListing onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ArticleListing.create(parent);
  }

  static class ArticleDiff extends DiffUtil.ItemCallback<Article> {
    @Override
    public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
      return oldItem.title.equals(newItem.title);
    }
  }
}
