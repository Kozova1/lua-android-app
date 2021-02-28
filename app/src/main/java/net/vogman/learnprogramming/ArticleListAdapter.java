package net.vogman.learnprogramming;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArticleListAdapter extends ListAdapter<Article, Listing<Article>> {
  public ArticleListAdapter(@NotNull DiffUtil.ItemCallback<Article> diffCallback) {
    super(diffCallback);
  }

  @Override
  public void onBindViewHolder(@NonNull Listing<Article> holder, int position) {
    Article current = getItem(position);
    holder.bindTo(current);
  }

  @NonNull
  @Override
  public Listing<Article> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return Listing.createArticle(parent);
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
