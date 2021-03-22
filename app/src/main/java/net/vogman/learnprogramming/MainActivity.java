package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;


public class MainActivity extends AppCompatActivity {
  private DrawerLayout drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FragmentManager supportFragmentManager = getSupportFragmentManager();

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

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
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == R.id.menu_reset_database) {
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
    } else if (itemId == R.id.menu_share_database) {
      ExerciseRepository exRepo = new ExerciseRepository(getApplication());
      exRepo.getExercises().observe(this, exercises -> {
        ArticleRepository arRepo = new ArticleRepository(getApplication());
        arRepo.getArticles().observe(this, articles -> {
          Gson gson = new Gson();
          ArticleAndExerciseHolder holder = new ArticleAndExerciseHolder(articles, exercises);
          String jsonResult = gson.toJson(holder);
          Intent i = new Intent();
          try {
            File f = File.createTempFile("database.", ".json-export", getFilesDir());
            try (FileWriter ostream = new FileWriter(f)) {
              ostream.write(jsonResult);
            }
            Uri uri = FileProvider.getUriForFile(this, "net.vogman.learnprogramming.fileprovider", f);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            i.putExtra(Intent.EXTRA_TITLE, "Export Database");
            i.setType("text/plain");
            startActivity(Intent.createChooser(i, getResources().getString(R.string.share_database)));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      });
    } else if (itemId == R.id.menu_export_database) {
      Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("application/octet-stream");
      i.putExtra(Intent.EXTRA_TITLE, "database-export.json");
      startActivityForResult(i, 1);
    } else if (itemId == R.id.menu_import_database) {
      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
      i.setType("*/*");
      startActivityForResult(i, 2);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
      Uri result = data.getData();
      new ExerciseRepository(getApplication()).getExercises().observe(this, exercises ->
        new ArticleRepository(getApplication()).getArticles().observe(this, articles -> {
          Gson gson = new Gson();
          ArticleAndExerciseHolder holder = new ArticleAndExerciseHolder(articles, exercises);
          String jsonResult = gson.toJson(holder);

          try (OutputStream os = getContentResolver().openOutputStream(result)) {
            try (PrintWriter wr = new PrintWriter(os)) {
              wr.print(jsonResult);
            }
          } catch (IOException e) {
            e.printStackTrace();
            return;
          }

          Snackbar.make(drawer, "Database saved", BaseTransientBottomBar.LENGTH_SHORT);
        }));
    } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
      Uri res = data.getData();
      try (InputStream ifs = getContentResolver().openInputStream(res)) {
        try (InputStreamReader reader = new InputStreamReader(ifs)) {
          Gson gson = new GsonBuilder().setLenient().create();
          ArticleAndExerciseHolder holder = gson.fromJson(reader, ArticleAndExerciseHolder.class);
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
          Snackbar.make(drawer, "Imported successfully!", BaseTransientBottomBar.LENGTH_SHORT);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JsonSyntaxException e) {
        Snackbar.make(drawer, "Backup is corrupted. Did not import.", BaseTransientBottomBar.LENGTH_SHORT);
        e.printStackTrace();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void setMenuItemWhite(MenuItem item) {
    SpannableString sItem = new SpannableString(item.getTitle());
    sItem.setSpan(new TextAppearanceSpan(this, R.style.MenuText), 0, sItem.length(), 0);
    item.setTitle(sItem);
  }
}