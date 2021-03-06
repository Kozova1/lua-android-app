package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private BroadcastReceiver airplaneNagger = null;

    private void finishCourse() {
        FirstTimeActivity.endCourse(getApplicationContext());
        startActivity(new Intent(this, FirstTimeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        airplaneNagger = new AirplaneModeDuolingoNotificationSender();
        registerReceiver(airplaneNagger, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));

        {
            Bundle args = new Bundle();
            args.putString(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_ARTICLE);
            ListFragment frag = new ListFragment();
            frag.setArguments(args);

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        {
            Menu drawerMenu = navigationView.getMenu();
            setMenuItemWhite(drawerMenu.findItem(R.id.general_label));
            setMenuItemWhite(drawerMenu.findItem(R.id.get_info_label));
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_articles) {
                // Articles selected
                ListFragment frag = new ListFragment();
                Bundle args = new Bundle();
                args.putString(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_ARTICLE);
                frag.setArguments(args);
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
            } else if (id == R.id.nav_exercises) {
                ListFragment frag = new ListFragment();
                Bundle args = new Bundle();
                args.putString(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_EXERCISE);
                frag.setArguments(args);
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
            } else if (id == R.id.nav_add) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new AddContentFragment()).commit();
            } else if (id == R.id.nav_info_creating_articles) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new CreatingArticlesInfoFragment()).commit();
            } else if (id == R.id.nav_info_creating_exercises) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new CreatingExercisesInfoFragment()).commit();
            } else if (id == R.id.nav_repl) {
                try {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new ReplFragment()).commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_OpenName, R.string.nav_drawer_ClosedName);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_main, menu);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean doNotNag = prefs.getBoolean("doNotNag", false);
        menu.findItem(R.id.menu_nag_on_airplane_mode).setChecked(doNotNag);

        if (doNotNag) {
            try {
                unregisterReceiver(airplaneNagger);
            } catch (IllegalArgumentException e) {
                // Oops, didn't register it yet.
                // No worries, we just ignore it.
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reset_course) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Reset")
                    .setMessage("Reset Database?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        AppDatabase.databaseWriteExecutor.execute(() -> {
                            AppDatabase db = AppDatabase.getDatabase(this);
                            db.articleDao().clear();
                            db.exerciseDao().clear();
                        });
                        Snackbar.make(drawer, "Database reset, Restart app for changes to take effect", BaseTransientBottomBar.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else if (itemId == R.id.menu_publish_course) {
            ExerciseRepository exRepo = new ExerciseRepository(getApplication());
            exRepo.getExercises().observe(this, exercises -> {
                ArticleRepository arRepo = new ArticleRepository(getApplication());
                arRepo.getArticles().observe(this, articles -> {
                    Gson gson = new Gson();
                    Course holder = new Course(articles, exercises);
                    String jsonResult = gson.toJson(holder);
                    StringBuilder randomId = new StringBuilder();
                    Random prng = new Random();
                    for (int i = 0; i < 8; i++) {
                        randomId.append(prng.nextInt(10));
                    }
                    String id = randomId.toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(id);
                    ref.setValue(jsonResult);
                    ClipboardManager mgr = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    mgr.setPrimaryClip(ClipData.newPlainText("Course ID", id));
                    Snackbar snack = Snackbar.make(drawer, "Course published with ID: " + id + ". Copied to clipboard.", BaseTransientBottomBar.LENGTH_LONG);
                    snack.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            finishCourse();
                            super.onDismissed(transientBottomBar, event);
                        }
                    });
                    snack.show();
                });
            });
        } else if (itemId == R.id.menu_exit_course) {
            finishCourse();
        } else if (itemId == R.id.menu_nag_on_airplane_mode) {
            item.setChecked(!item.isChecked());
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            prefs.edit().putBoolean("doNotNag", item.isChecked()).apply();
            try {
                if (item.isChecked()) {
                    unregisterReceiver(airplaneNagger);
                } else {
                    registerReceiver(airplaneNagger, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
                }
            } catch (Exception ignored) {
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenuItemWhite(MenuItem item) {
        SpannableString sItem = new SpannableString(item.getTitle());
        sItem.setSpan(new TextAppearanceSpan(this, R.style.MenuText), 0, sItem.length(), 0);
        item.setTitle(sItem);
    }
}
