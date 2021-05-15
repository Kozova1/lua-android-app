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
import android.widget.TextView;

public class ListFragment extends Fragment {
  public static final String LIST_FRAGMENT_TYPE = "type";
  public static final String LIST_FRAGMENT_TYPE_ARTICLE = "Article";
  public static final String LIST_FRAGMENT_TYPE_EXERCISE = "Exercise";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list, container, false);

    TextView textView = view.findViewById(R.id.emptyTextView);
    RecyclerView recycler = view.findViewById(R.id.recyclerView);
    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

    Bundle args = requireArguments();
    String type = args.getString(LIST_FRAGMENT_TYPE);
    if (type.equals(LIST_FRAGMENT_TYPE_ARTICLE)) {
      final ArticleListAdapter adapter = new ArticleListAdapter(new ArticleListAdapter.ArticleDiff());
      recycler.setAdapter(adapter);

      ArticleListViewModel viewModel = new ViewModelProvider(this).get(ArticleListViewModel.class);
      viewModel.getArticles().observe(getViewLifecycleOwner(), articles -> {
        articles.sort((article1, article2) -> article1.uid - article2.uid);
        adapter.submitList(articles);
        textView.setText(R.string.no_articles_yet);
        if (articles.isEmpty()) {
          recycler.setVisibility(View.GONE);
          textView.setVisibility(View.VISIBLE);
        } else {
          recycler.setVisibility(View.VISIBLE);
          textView.setVisibility(View.GONE);
        }
      });
    } else if (type.equals(LIST_FRAGMENT_TYPE_EXERCISE)) {
      final ExerciseListAdapter adapter = new ExerciseListAdapter(new ExerciseListAdapter.ExerciseDiff());
      recycler.setAdapter(adapter);

      ExerciseListViewModel viewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);
      viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
        exercises.sort((exercise1, exercise2) -> exercise1.uid - exercise2.uid);
        adapter.submitList(exercises);
        textView.setText(R.string.no_exercises_yet);
        if (exercises.isEmpty()) {
          recycler.setVisibility(View.GONE);
          textView.setVisibility(View.VISIBLE);
        } else {
          recycler.setVisibility(View.VISIBLE);
          textView.setVisibility(View.GONE);
        }
      });
    } else {
      throw new IllegalArgumentException();
    }

    return view;
  }
}