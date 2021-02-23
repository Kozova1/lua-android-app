package net.vogman.learnprogramming;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CodeActivity extends AppCompatActivity {
  private CodeAssignment respectiveAssignment;

  private void setupView(CodeAssignment assignment) {
    this.respectiveAssignment = assignment;
    EditText codeIn = findViewById(R.id.codeIn);
    TextView preText = findViewById(R.id.preCodeText);
    TextView postText = findViewById(R.id.postCodeText);
    codeIn.setText(assignment.getTemplate());
    preText.setText(assignment.getPreText());
    postText.setText(assignment.getPostText());
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_code);
    Bundle intentArgs = getIntent().getExtras();
    assert intentArgs != null;
    Object assignment = intentArgs.get("assignment");
    assert assignment != null;

    setupView(new CodeAssignment((String[]) assignment));
  }
}
