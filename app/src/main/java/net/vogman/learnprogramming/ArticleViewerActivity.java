package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
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

public class ArticleViewerActivity extends AppCompatActivity {

  private int id;
  private String contents;
  private String title;
  private TextView contentView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_article_viewer);
    Bundle args = getIntent().getExtras();
    this.contents = args.getString("contents");
    this.title = args.getString("title");
    this.id = args.getInt("id");

    final ActionBar bar = getSupportActionBar();
    assert bar != null;
    bar.setTitle(this.title);
    bar.setDisplayHomeAsUpEnabled(true);
    this.contentView = findViewById(R.id.articleViewerContent);

    final Prism4j prism4j = new Prism4j(new AppGrammarLocator());

    final Markwon markwon = Markwon.builder(this)
      .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
      .build();
    markwon.setMarkdown(contentView, contents);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ArticleListViewModel viewModel = new ViewModelProvider(this).get(ArticleListViewModel.class);
    viewModel.markAsRead(this.id);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}