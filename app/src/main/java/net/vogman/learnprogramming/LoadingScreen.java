package net.vogman.learnprogramming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_loading_screen);

      ProgressBar pbar = findViewById(R.id.progressBar);
      pbar.setIndeterminate(true);

      Thread loader = new Thread() {
        @Override
        public void run() {
          String[] assignment = null;
          try {
            super.run();
            Database db = new Database();
            assignment = db.fetchCodeAssignment(getIntent().getExtras().get("codeId").toString());
          } catch (Exception e) {
            Log.e("Loading Screen", e.getLocalizedMessage());
          } finally {
            Intent i = new Intent(LoadingScreen.this, CodeActivity.class);
            i.putExtra("assignment", assignment);
            startActivity(i);
          }
        }
      };
      loader.start();
    }
}