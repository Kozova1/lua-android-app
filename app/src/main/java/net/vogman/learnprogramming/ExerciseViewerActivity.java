package net.vogman.learnprogramming;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import io.noties.markwon.Markwon;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;

public class ExerciseViewerActivity extends AppCompatActivity {

  private int id;
  private String instructions;
  private String title;
  private String test;
  private String template;
  private TextView instructionView;
  private TextView resultsView;
  private EditText codeView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_viewer);
    Bundle args = getIntent().getExtras();

    this.title = args.getString("title");
    this.instructions = args.getString("instructions");
    this.template = args.getString("template");
    this.test = args.getString("test");
    this.id = args.getInt("id");

    final ActionBar bar = getSupportActionBar();
    assert bar != null;
    bar.setTitle(this.title);
    bar.setDisplayHomeAsUpEnabled(true);

    this.instructionView = findViewById(R.id.exerciseViewerInstructions);
    this.resultsView = findViewById(R.id.exerciseResults);

    final Prism4j prism4j = new Prism4j(new AppGrammarLocator());

    final Markwon markwon = Markwon.builder(this)
      .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
      .build();
    markwon.setMarkdown(instructionView, instructions);
    codeView = findViewById(R.id.code_editor);
    codeView.setText(this.template);

    findViewById(R.id.exerciseFAB).setOnClickListener(view -> {
      // Ugly hack to prevent keyboard hiding snackbar
      InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

      resultsView.setText(""); // clear error screen
      Globals env = JsePlatform.standardGlobals();
      Snackbar message;
      LuaValue res;
      boolean result;
      try {
        env.load(this.codeView.getText().toString()).call();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(view, "Exception encountered when loading your code.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }
      try {
        env.load(this.test).call();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(view, "Exception encountered when loading the test code.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }
      try {
        res = env.get("test").call(); // this gets the "test" function and calls it.
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(view, "Test did not pass. Try again!", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }

      try {
        result = res.checkboolean();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(view, "Test did not return a boolean.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }

      if (result) {
        message = Snackbar.make(view, "Test passed. Success!", BaseTransientBottomBar.LENGTH_SHORT);
        ExerciseRepository repo = new ExerciseRepository(getApplication());
        repo.markAsDone(this.id, true, this.codeView.getText().toString());
      } else {
        message = Snackbar.make(view, "Test did not pass. Try again!", BaseTransientBottomBar.LENGTH_SHORT);
      }
      message.show();
    });
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