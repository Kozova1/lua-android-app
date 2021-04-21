package net.vogman.learnprogramming;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Exercise implements Listable {
  /**
   * Create table with this command:
   * CREATE TABLE IF NOT EXISTS Exercise (
   *  uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
   *  Title TEXT NOT NULL,
   *  Test TEXT NOT NULL,
   *  Instructions TEXT NOT NULL,
   *  Template TEXT NOT NULL,
   *  IsDone INTEGER NOT NULL,
   *  IsHard INTEGER NOT NULL,
   *
   * );
   * */
  @PrimaryKey(autoGenerate = true)
  public int uid;

  @ColumnInfo(name = "Title")
  @NonNull
  public String title;

  @ColumnInfo(name = "Test")
  @NonNull
  public String test;

  @ColumnInfo(name = "Instructions")
  @NonNull
  public String instructions;

  @ColumnInfo(name = "Template")
  @NonNull
  public String template;

  @ColumnInfo(name = "IsDone")
  public boolean isDone;

  @ColumnInfo(name = "IsHard")
  public boolean isHard;

  public Exercise(@NonNull String title, @NonNull String test, @NonNull String instructions, @NonNull String template, boolean isHard) {
    this.title = title;
    this.test = test;
    this.instructions = instructions;
    this.template = template;
    this.isHard = isHard;
  }
}
