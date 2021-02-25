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

public class AddExerciseFragment extends Fragment {
  private EditText exerciseInstructions;
  private EditText exerciseTitle;
  private EditText exerciseTest;
  private EditText exerciseTemplate;
  private CheckBox isHardCheckbox;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_exercise, container, false);

    final Markwon markwon = Markwon.create(view.getContext());
    final MarkwonEditor editor = MarkwonEditor.create(markwon);
    exerciseInstructions = view.findViewById(R.id.exerciseInstructions);
    exerciseTitle = view.findViewById(R.id.exerciseTitle);
    exerciseTest = view.findViewById(R.id.exerciseTest);
    exerciseTemplate = view.findViewById(R.id.exerciseTemplate);
    isHardCheckbox = view.findViewById(R.id.exerciseIsHard);

    exerciseInstructions.addTextChangedListener(
      MarkwonEditorTextWatcher.withPreRender(
        editor,
        Executors.newCachedThreadPool(),
        exerciseInstructions
      ));

    FloatingActionButton fab = view.findViewById(R.id.submitFAB);
    fab.setOnClickListener(vFAB -> {
      String title = exerciseTitle.getText().toString();
      String instructions = exerciseInstructions.getText().toString();
      String template = exerciseTemplate.getText().toString();
      String test = exerciseTest.getText().toString();
      boolean isHard = isHardCheckbox.isChecked();
      ExerciseRepository repo = new ExerciseRepository((Application) view.getContext().getApplicationContext());
      repo.addExercise(new Exercise(title, test, instructions, template, isHard));
      Snackbar.make(view, "Added exercise successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
    });

    return view;
  }
}