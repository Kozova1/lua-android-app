package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {
  private DrawerLayout drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    FragmentManager supportFragmentManager = getSupportFragmentManager();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(item -> {
      int id = item.getItemId();
      if (id == R.id.nav_articles) {
        // Articles selected
        ListFragment frag = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_ARTICLE);
        frag.setArguments(args);
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
      } else if (id == R.id.nav_home) {
        // Home selected
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
      } else if (id == R.id.nav_exercises) {
        ListFragment frag = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_EXERCISE);
        frag.setArguments(args);
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
      } else if (id == R.id.nav_add) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new AddContentFragment()).commit();
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
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();
    FragmentManager supportFragmentManager = getSupportFragmentManager();
    if (itemId == R.id.menu_creating_articles) {
      supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new CreatingArticlesInfoFragment()).commit();
    } else if (itemId == R.id.menu_creating_exercises) {
      supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new CreatingExercisesInfoFragment()).commit();
    } else if (itemId == R.id.menu_reset_database) {
      new AlertDialog.Builder(this)
        .setTitle("Confirm Reset")
        .setMessage("Reset Database?")
        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
          AppDatabase.databaseWriteExecutor.execute(AppDatabase.getDatabase(this)::clearAllTables);
          Snackbar.make(drawer, "Database reset, Restart app for changes to take effect", BaseTransientBottomBar.LENGTH_SHORT).show();
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
    }
    return super.onOptionsItemSelected(item);
  }
}