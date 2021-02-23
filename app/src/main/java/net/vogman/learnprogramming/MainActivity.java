package net.vogman.learnprogramming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void moveToCodeActivity(String codeId) {
      Intent i = new Intent(MainActivity.this, LoadingScreen.class);
      i.putExtra("codeId", codeId);
      startActivity(i);
    }
}