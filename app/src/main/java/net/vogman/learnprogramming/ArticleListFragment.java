package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArticleListFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_article_list, container, false);

    RecyclerView recycler = view.findViewById(R.id.articlesRecyclerView);
    final ArticleListAdapter adapter = new ArticleListAdapter(new ArticleListAdapter.ArticleDiff());

    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    recycler.setAdapter(adapter);

    ArticleListViewModel viewModel = new ViewModelProvider(this).get(ArticleListViewModel.class);
    viewModel.getArticles().observe(getViewLifecycleOwner(), articles -> {
      articles.sort((article1, article2) -> article1.uid - article2.uid);
      adapter.submitList(articles);
    });

    return view;
  }
}