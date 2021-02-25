package net.vogman.learnprogramming;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ExerciseListing extends RecyclerView.ViewHolder {
  private String title;
  private String instructions;
  private String template;
  private String test;
  private int id;
  private boolean isDone;
  private final ImageView checkImageView;
  private final ImageView hardImageView;
  private final TextView titleTextView;
  private boolean isHard;

  public ExerciseListing(View view) {
    super(view);
    this.title = view.getResources().getString(R.string.titlePlaceholder);
    this.instructions = "Empty";
    this.template = "Empty-NoCode";
    this.test = "function test() return true end";
    this.isDone = false;
    this.checkImageView = view.findViewById(R.id.readTickImageView);
    checkImageView.setImageResource(R.drawable.ic_notread_24px);
    this.hardImageView = view.findViewById(R.id.exerciseHardIconImageView);
    hardImageView.setVisibility(View.INVISIBLE);
    this.titleTextView = view.findViewById(R.id.exerciseListingTitleTextView);
    titleTextView.setText(this.title);
    View.OnClickListener clickListener = v -> {
      Intent i = new Intent(v.getContext(), ExerciseViewerActivity.class);
      i.putExtra("instructions", instructions);
      i.putExtra("template", template);
      i.putExtra("title", title);
      i.putExtra("id", id);
      i.putExtra("test", test);
      v.getContext().startActivity(i);
    };
    view.setOnClickListener(clickListener);
    view.findViewById(R.id.readTickImageView).setOnClickListener(v -> {
      ImageView nv = (ImageView) v;
      isDone = !isDone;
      nv.setImageResource(isDone ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
      ExerciseRepository repo = new ExerciseRepository((Application) view.getContext().getApplicationContext());
      repo.markAsDone(this.id, isDone, template);
    });
  }

  static ExerciseListing create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.exercise_listing, parent, false);
    return new ExerciseListing(view);
  }

  public String title() {
    return this.title;
  }
  public void title(String newTitle) {
    this.title = newTitle;
    titleTextView.setText(this.title);
  }

  public boolean done() {
    return this.isDone;
  }

  public void done(boolean newDone) {
    this.isDone = newDone;
    checkImageView.setImageResource(newDone ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
  }

  public String instructions() {
    return this.instructions;
  }

  public void instructions(String instructions) {
    this.instructions = instructions;
  }

  public String test() {
    return this.test;
  }

  public void test(String test) {
    this.test = test;
  }

  public int id() {
    return this.id;
  }

  public void id(int id) {
    this.id = id;
  }

  public String template() { return this.template; }
  public void template(String newTemplate) {
    this.template = newTemplate;
  }

  public boolean hard() {
    return this.isHard;
  }

  public void hard(boolean isHard) {
    this.isHard = isHard;
    hardImageView.setVisibility(isHard ? View.VISIBLE : View.INVISIBLE);
  }
}