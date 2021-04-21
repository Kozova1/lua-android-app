package net.vogman.learnprogramming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FirstTimeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_first_time);

    Button skipBtn = findViewById(R.id.create_course_btn);
    Button importBtn = findViewById(R.id.import_course_btn);
    skipBtn.setOnClickListener(v -> finish());
    importBtn.setOnClickListener(v -> {
      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
      i.setType("*/*");
      startActivityForResult(i, 2);
    });
  }

  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
      Uri res = data.getData();
      try (InputStream ifs = getContentResolver().openInputStream(res)) {
        try (InputStreamReader reader = new InputStreamReader(ifs)) {
          Gson gson = new GsonBuilder().setLenient().create();
          Course holder = gson.fromJson(reader, Course.class);
          AppDatabase.databaseWriteExecutor.execute(() -> {
            ExerciseDao dao = AppDatabase.getDatabase(this).exerciseDao();
            dao.clear();
            dao.insertAll(holder.exercises);
          });
          AppDatabase.databaseWriteExecutor.execute(() -> {
            ArticleDao dao = AppDatabase.getDatabase(this).articleDao();
            dao.clear();
            dao.insertAll(holder.articles);
          });
          Snackbar.make(findViewById(R.id.first_time_parent), "Imported successfully!", BaseTransientBottomBar.LENGTH_SHORT).show();
          finish();
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JsonSyntaxException e) {
        Snackbar.make(findViewById(R.id.first_time_parent), "Backup is corrupted. Did not import.", BaseTransientBottomBar.LENGTH_SHORT).show();
        e.printStackTrace();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
