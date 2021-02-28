package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

public class ViewerActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_viewer);
    Bundle args = getIntent().getExtras();

    String title = args.getString("title");

    Toolbar toolbar = findViewById(R.id.viewerToolbar);
    setSupportActionBar(toolbar);

    final ActionBar bar = getSupportActionBar();
    assert bar != null;
    bar.setTitle(title);
    bar.setDisplayHomeAsUpEnabled(true);

    String t = args.getString(ListFragment.LIST_FRAGMENT_TYPE);
    Fragment frag;
    if (t.equals(ListFragment.LIST_FRAGMENT_TYPE_ARTICLE)) {
      frag = new ArticleFragment();
    } else if (t.equals(ListFragment.LIST_FRAGMENT_TYPE_EXERCISE)) {
      frag = new ExerciseFragment();
    } else {
      throw new IllegalArgumentException();
    }
    args.remove(ListFragment.LIST_FRAGMENT_TYPE);
    frag.setArguments(args);
    getSupportFragmentManager().beginTransaction().replace(R.id.viewer_fragment_target, frag).commit();
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}