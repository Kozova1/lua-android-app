package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.noties.markwon.Markwon;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.annotations.PrismBundle;

@PrismBundle(
  include = {"javascript"},
  grammarLocatorClassName = ".AppGrammarLocator"
)

public class ArticleFragment extends Fragment {

  private int id;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_article, container, false);
    Bundle args = requireArguments();
    String contents = args.getString("contents");
    this.id = args.getInt("id");

    TextView contentView = view.findViewById(R.id.articleViewerContent);

    final Prism4j prism4j = new Prism4j(new AppGrammarLocator());

    final Markwon markwon = Markwon.builder(view.getContext())
      .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
      .build();
    markwon.setMarkdown(contentView, contents);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ArticleRepository repo = new ArticleRepository(requireActivity().getApplication());
    repo.markRead(this.id, true);
  }
}