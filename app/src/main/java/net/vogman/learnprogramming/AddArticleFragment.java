package net.vogman.learnprogramming;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class AddArticleFragment extends Fragment {
  private EditText articleContent;
  private EditText articleTitle;
  private CheckBox isHardCheckbox;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_article, container, false);

    final Markwon markwon = Markwon.create(view.getContext());
    final MarkwonEditor editor = MarkwonEditor.create(markwon);
    articleContent = view.findViewById(R.id.articleContent);
    articleTitle = view.findViewById(R.id.articleTitle);
    isHardCheckbox = view.findViewById(R.id.articleIsHard);

    articleContent.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
      editor,
      Executors.newCachedThreadPool(),
      articleContent));

    FloatingActionButton fab = view.findViewById(R.id.submitFAB);
    fab.setOnClickListener(vFAB -> {
      String title = articleTitle.getText().toString();
      String content = articleContent.getText().toString();
      boolean isHard = isHardCheckbox.isChecked();
      ArticleRepository repo = new ArticleRepository((Application) view.getContext().getApplicationContext());
      repo.addArticle(new Article(title, content, isHard));
      Snackbar.make(view, "Added article successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
    });

    return view;
  }
}