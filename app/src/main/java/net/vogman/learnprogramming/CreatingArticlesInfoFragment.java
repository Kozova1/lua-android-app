package net.vogman.learnprogramming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.noties.markwon.Markwon;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;

public class CreatingArticlesInfoFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_info, container, false);

    TextView contentTarget = view.findViewById(R.id.textContent);
    final Prism4j prism4j = new Prism4j(new AppGrammarLocator());
    final Markwon markwon = Markwon.builder(view.getContext())
      .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
      .build();
    markwon.setMarkdown(contentTarget, getResources().getString(R.string.creating_articles_info));

    return view;
  }
}