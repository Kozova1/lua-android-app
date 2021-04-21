package net.vogman.learnprogramming;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

public class ExerciseFragment extends Fragment {

  private int id;
  private String test;
  private TextView resultsView;
  private EditText codeView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_view_exercise, container, false);
    Bundle args = requireArguments();


    String instructions = args.getString("instructions");
    String template = args.getString("template");
    this.test = args.getString("test");
    this.id = args.getInt("id");

    this.resultsView = view.findViewById(R.id.exerciseResults);

    TextView instructionView = view.findViewById(R.id.exerciseViewerInstructions);
    final Prism4j prism4j = new Prism4j(new AppGrammarLocator());

    final Markwon markwon = Markwon.builder(view.getContext())
            .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
            .build();
    markwon.setMarkdown(instructionView, instructions);
    codeView = view.findViewById(R.id.code_editor);
    codeView.setText(template);

    view.findViewById(R.id.exerciseFAB).setOnClickListener(v -> {
      // Ugly hack to prevent keyboard hiding snackbar
      InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

      resultsView.setText(""); // clear error screen
      Globals env = JsePlatform.standardGlobals();
      Snackbar message;
      LuaValue res;
      boolean result;
      try {
        env.load(this.codeView.getText().toString()).call();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(v, "Exception encountered when loading your code.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }
      try {
        env.load(this.test).call();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(v, "Exception encountered when loading the test code.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }
      try {
        res = env.get("test").call(); // this gets the "test" function and calls it.
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(v, "Test did not pass. Try again!", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }

      try {
        result = res.checkboolean();
      } catch (LuaError e) {
        resultsView.setText(e.getLocalizedMessage());
        message = Snackbar.make(v, "Test did not return a boolean.", BaseTransientBottomBar.LENGTH_SHORT);
        message.show();
        return;
      }

      if (result) {
        message = Snackbar.make(v, "Test passed. Success!", BaseTransientBottomBar.LENGTH_SHORT);
        ExerciseRepository repo = new ExerciseRepository(requireActivity().getApplication());
        repo.markAsDone(this.id, true, this.codeView.getText().toString());
      } else {
        message = Snackbar.make(v, "Test did not pass. Try again!", BaseTransientBottomBar.LENGTH_SHORT);
      }
      message.show();
    });
    return view;
  }
}