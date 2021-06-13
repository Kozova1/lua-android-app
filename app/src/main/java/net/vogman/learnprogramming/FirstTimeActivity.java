package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstTimeActivity extends AppCompatActivity {
    private final static String IN_COURSE = "isInCourse";

    private void moveToNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
        finish();
    }

    protected static boolean isInCourse(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(IN_COURSE, false);
    }

    protected static void beginCourse(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(IN_COURSE, true);
        edit.apply();
    }

    protected static void endCourse(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(IN_COURSE, false);
        edit.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirstTimeActivity.isInCourse(getApplicationContext())) {
            moveToNextActivity();
        }

        setContentView(R.layout.activity_first_time);

        EditText joinCourseId = findViewById(R.id.import_course_id);
        Button joinCourseBtn = findViewById(R.id.import_course_btn);
        Button createCourseBtn = findViewById(R.id.create_course_btn);
        Activity act = this;

        joinCourseBtn.setOnClickListener(v -> {
            if (joinCourseId.getText().length() == 8) {
                String courseId = joinCourseId.getText().toString();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference(courseId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Snackbar.make(v, "Invalid Course ID!", BaseTransientBottomBar.LENGTH_LONG).show();
                            return;
                        }
                        String asJson = snapshot.getValue(String.class);
                        Course course = new Course(asJson);
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            ArticleDao articleDao = AppDatabase.getDatabase(act).articleDao();
                            ExerciseDao exerciseDao = AppDatabase.getDatabase(act).exerciseDao();
                            articleDao.clear();
                            exerciseDao.clear();
                            articleDao.insertAll(course.articles);
                            exerciseDao.insertAll(course.exercises);
                        });
                        beginCourse(getApplicationContext());
                        moveToNextActivity();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(v, "Invalid Course ID!", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
                ref.get();
            } else {
                Snackbar.make(v, "Course ID is exactly 8 digits long", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        createCourseBtn.setOnClickListener(v -> {
            beginCourse(getApplicationContext());
            AppDatabase.databaseWriteExecutor.execute(() -> {
                ArticleDao articleDao = AppDatabase.getDatabase(act).articleDao();
                ExerciseDao exerciseDao = AppDatabase.getDatabase(act).exerciseDao();
                articleDao.clear();
                exerciseDao.clear();
            });
            moveToNextActivity();
        });
    }
}
