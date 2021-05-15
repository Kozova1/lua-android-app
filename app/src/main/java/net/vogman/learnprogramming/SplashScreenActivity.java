package net.vogman.learnprogramming;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.noties.markwon.Markwon;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String SPLASH_PREF_STRING = "splashShown";

    private void moveToNextActivity() {
        Intent i = new Intent(this, FirstTimeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (wasSplashShownBefore(this)) {
            moveToNextActivity();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        FloatingActionButton fab = findViewById(R.id.splash_continue);
        fab.setOnClickListener(v -> {
            setSplashShown(this);
            moveToNextActivity();
        });

        TextView splashContentView = findViewById(R.id.splashscreen_content);
        final Prism4j prism4j = new Prism4j(new AppGrammarLocator());

        final Markwon markwon = Markwon.builder(this)
                .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
                .build();
        markwon.setMarkdown(splashContentView, getResources().getString(R.string.introduction_md));
    }

    protected static boolean wasSplashShownBefore(Activity act) {
        SharedPreferences prefs = act.getPreferences(MODE_PRIVATE);
        return prefs.getBoolean(SPLASH_PREF_STRING, false);
    }

    protected static void setSplashShown(Activity act) {
        SharedPreferences prefs = act.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SPLASH_PREF_STRING, true);
        edit.apply();
    }
}