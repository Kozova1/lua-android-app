package net.vogman.learnprogramming;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Listing<T extends Listable> extends RecyclerView.ViewHolder {
  public enum ListingType {
    Article, Exercise
  }

  private final ListingType listingType;
  private String title;
  private String instructions = "Empty";
  private String template = "Empty-NoCode";
  private String test = "function test() return true end";
  private String contents = "Empty";
  private int id;
  private boolean isDone = false;
  private boolean hasBeenRead = false;
  private final ImageView checkImageView;
  private final ImageView hardImageView;
  private final TextView titleTextView;

  public Listing(View view, ListingType type) {
    super(view);

    title = view.getResources().getString(R.string.titlePlaceholder);

    listingType = type;

    this.checkImageView = view.findViewById(R.id.readTickImageView);
    checkImageView.setImageResource(R.drawable.ic_notread_24px);

    this.hardImageView = view.findViewById(R.id.hardIconImageView);
    hardImageView.setVisibility(View.INVISIBLE);

    this.titleTextView = view.findViewById(R.id.listingTitleTextView);
    titleTextView.setText(this.title);

    ImageView typeImageView = view.findViewById(R.id.typeIconImageView);
    if (type == ListingType.Article) {
      typeImageView.setImageResource(R.drawable.ic_article_24px);
      view.setOnClickListener(v -> {
        Intent i = new Intent(v.getContext(), ViewerActivity.class);
        i.putExtra(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_ARTICLE);
        i.putExtra("contents", contents);
        i.putExtra("title", title);
        i.putExtra("id", id);
        v.getContext().startActivity(i);
      });
      view.findViewById(R.id.readTickImageView).setOnClickListener(v -> {
        ImageView nv = (ImageView) v;
        hasBeenRead = !hasBeenRead;
        nv.setImageResource(hasBeenRead ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
        ArticleRepository repo = new ArticleRepository((Application) view.getContext().getApplicationContext());
        repo.markRead(this.id, hasBeenRead);
      });
    } else if (type == ListingType.Exercise) {
      typeImageView.setImageResource(R.drawable.ic_exercise_24px);
      view.setOnClickListener(v -> {
        Intent i = new Intent(v.getContext(), ViewerActivity.class);
        i.putExtra(ListFragment.LIST_FRAGMENT_TYPE, ListFragment.LIST_FRAGMENT_TYPE_EXERCISE);
        i.putExtra("instructions", instructions);
        i.putExtra("template", template);
        i.putExtra("title", title);
        i.putExtra("id", id);
        i.putExtra("test", test);
        v.getContext().startActivity(i);
      });

      view.findViewById(R.id.readTickImageView).setOnClickListener(v -> {
        ImageView nv = (ImageView) v;
        isDone = !isDone;
        nv.setImageResource(isDone ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
        ExerciseRepository repo = new ExerciseRepository((Application) view.getContext().getApplicationContext());
        repo.markAsDone(this.id, isDone, template);
      });

      view.findViewById(R.id.deleteImageView).setOnClickListener(v ->
        new AlertDialog.Builder(view.getContext())
          .setTitle("Confirm Deletion")
          .setMessage("Delete this exercise?")
          .setPositiveButton(android.R.string.yes, (dialog, which) ->
            new ExerciseRepository((Application) view.getContext().getApplicationContext()).remove(this.id))
          .setNegativeButton(android.R.string.no, null)
          .show()
      );
    } else { // THIS SHOULD **NEVER** HAPPEN
      throw new IllegalStateException();
    }
  }

  static Listing<Exercise> createExercise(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.listing, parent, false);
    return new Listing<>(view, ListingType.Exercise);
  }

  static Listing<Article> createArticle(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.listing, parent, false);
    return new Listing<>(view, ListingType.Article);
  }

  public void bindTo(T bound) {
    boolean isHard = false;
    if (listingType == ListingType.Exercise && bound instanceof Exercise) {
      // No binding to articles when selected as exercise!
      Exercise exercise = (Exercise) bound;
      this.title = exercise.title;
      titleTextView.setText(this.title);

      this.isDone = exercise.isDone;
      checkImageView.setImageResource(
        exercise.isDone
          ? R.drawable.ic_wasread_24px
          : R.drawable.ic_notread_24px
      );
      this.instructions = exercise.instructions;
      this.test = exercise.test;
      this.id = exercise.uid;
      this.template = exercise.template;
      isHard = exercise.isHard;
      hardImageView.setVisibility(isHard ? View.VISIBLE : View.INVISIBLE);
    } else if (listingType == ListingType.Article && bound instanceof Article) {
      Article article = (Article) bound;
      this.title = article.title;
      titleTextView.setText(this.title);

      this.hasBeenRead = article.hasBeenRead;
      checkImageView.setImageResource(hasBeenRead ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);

      this.contents = article.contents;
      this.id = article.uid;
      isHard = article.isHard;
      hardImageView.setVisibility(isHard ? View.VISIBLE : View.INVISIBLE);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
