package net.vogman.learnprogramming;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ArticleListing extends RecyclerView.ViewHolder {
  private String title;
  private String contents;
  private int id;
  private boolean wasRead;
  private final ImageView checkImageView;
  private final ImageView hardImageView;
  private final TextView titleTextView;
  private boolean isHard;

  public ArticleListing(View view) {
    super(view);
    this.title = view.getResources().getString(R.string.titlePlaceholder);
    this.contents = "Empty";
    this.wasRead = false;
    this.checkImageView = view.findViewById(R.id.readTickImageView);
    checkImageView.setImageResource(R.drawable.ic_notread_24px);
    this.hardImageView = view.findViewById(R.id.articleHardIconImageView);
    hardImageView.setVisibility(View.INVISIBLE);
    this.titleTextView = view.findViewById(R.id.articleListingTitleTextView);
    titleTextView.setText(this.title);
    View.OnClickListener clickListener = v -> {
      Intent i = new Intent(v.getContext(), ArticleViewerActivity.class);
      i.putExtra("contents", contents);
      i.putExtra("title", title);
      i.putExtra("id", id);
      v.getContext().startActivity(i);
    };
    view.setOnClickListener(clickListener);
    view.findViewById(R.id.readTickImageView).setOnClickListener(v -> {
      ImageView nv = (ImageView) v;
      wasRead = !wasRead;
      nv.setImageResource(wasRead ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
      ArticleRepository repo = new ArticleRepository((Application) view.getContext().getApplicationContext());
      repo.markRead(this.id, wasRead);
    });
  }

  static ArticleListing create(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.article_listing, parent, false);
    return new ArticleListing(view);
  }

  public String title() {
    return this.title;
  }
  public void title(String newTitle) {
    this.title = newTitle;
    titleTextView.setText(this.title);
  }

  public boolean read() {
    return this.wasRead;
  }

  public void read(boolean newRead) {
    this.wasRead = newRead;
    checkImageView.setImageResource(wasRead ? R.drawable.ic_wasread_24px : R.drawable.ic_notread_24px);
  }

  public String contents() {
    return this.contents;
  }

  public void contents(String contents) {
    this.contents = contents;
  }

  public int id() {
    return this.id;
  }

  public void id(int id) {
    this.id = id;
  }

  public boolean hard() {
    return this.isHard;
  }

  public void hard(boolean isHard) {
    this.isHard = isHard;
    hardImageView.setVisibility(isHard ? View.VISIBLE : View.INVISIBLE);
  }
}