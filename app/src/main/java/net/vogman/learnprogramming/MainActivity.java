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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new ArticleListFragment()).commit();
      } else if (id == R.id.nav_home) {
        // Home selected
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
      } else if (id == R.id.nav_exercises) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, new ExerciseListFragment()).commit();
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
    if (itemId == R.id.menu_share) {
      // Share database
      File dbFile = getDatabasePath("AppDB").getAbsoluteFile();
      File exportDB = new File(getFilesDir(), "AppDB.db");
      try {
        exportDB.createNewFile(); // just in case file does not yet exist
        FileChannel istream = new FileInputStream(dbFile).getChannel();
        FileChannel ostream = new FileOutputStream(exportDB).getChannel();
        ostream.transferFrom(istream, 0, istream.size());
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", exportDB);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setType("application/vnd.sqlite3");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      } catch (FileNotFoundException e) {
        Snackbar.make(drawer, "Database file does not exist", BaseTransientBottomBar.LENGTH_SHORT).show();
        e.printStackTrace();
      } catch (IOException e) {
        Snackbar.make(drawer, "Could not read size of Database file", BaseTransientBottomBar.LENGTH_SHORT).show();
        e.printStackTrace();
      }
    } else if (itemId == R.id.menu_import) {
      Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("*/*");
      startActivityForResult(i, 1);
    } else if (itemId == R.id.menu_export) {
      // Export database

      Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
      i.addCategory(Intent.CATEGORY_OPENABLE);
      i.setType("application/vnd.sqlite3");
      i.putExtra(Intent.EXTRA_TITLE, "App Database.db");
      startActivityForResult(i, 1);

    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
      // Import request
      if (data != null) {
        Uri uri = data.getData();
        // TODO: make this do something
        return;
      }
    } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
      // Export request
      if (data != null) {
        Uri uri = data.getData();

        try {
          InputStream dbFile = new FileInputStream(getDatabasePath("AppDB"));
          copy(dbFile, getContentResolver().openOutputStream(uri));
          dbFile.close();
        } catch (FileNotFoundException e) {
          Snackbar.make(drawer, "File not found exception, read stacktrace.", BaseTransientBottomBar.LENGTH_SHORT).show();
          e.printStackTrace();
        } catch (IOException e) {
          Snackbar.make(drawer, "IO exception, read stacktrace.", BaseTransientBottomBar.LENGTH_SHORT).show();
          e.printStackTrace();
        }
        return;
      }
    }
    // Not our request (or result is not OK), forward to superclass.
    super.onActivityResult(requestCode, resultCode, data);
  }

  public static void copy(InputStream src, OutputStream dst) throws IOException {
    // Transfer bytes from in to out
    byte[] buf = new byte[1024];
    int len;
    while ((len = src.read(buf)) > 0) {
      dst.write(buf, 0, len);
    }
  }
}