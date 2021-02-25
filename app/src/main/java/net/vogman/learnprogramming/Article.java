package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Article {
  /**
   * Create table with this command:
   * CREATE TABLE IF NOT EXISTS Article (
   *  uid INTEGER PRIMARY KEY NOT NULL,
   *  Title TEXT,
   *  ArticleContents TEXT,
   *  HasBeenRead INTEGER NOT NULL
   * );
   */
  @PrimaryKey(autoGenerate = true)
  public int uid;

  @ColumnInfo(name = "Title")
  @NonNull
  public String title;

  @ColumnInfo(name = "ArticleContents")
  @NonNull
  public String contents;

  @ColumnInfo(name = "HasBeenRead")
  public boolean hasBeenRead;

  @ColumnInfo(name = "IsHard")
  public boolean isHard;

  public Article(@NotNull String title, @NotNull String contents, boolean isHard) {
    this.hasBeenRead = false;
    this.isHard = isHard;
    this.contents = contents;
    this.title = title;
  }
}
