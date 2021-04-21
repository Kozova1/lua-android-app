package net.vogman.learnprogramming;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class, Exercise.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  private static final String DB_NAME = "AppDB";
  private static volatile AppDatabase INSTANCE;
  private static final int NUMBER_OF_THREADS = 4;
  static final ExecutorService databaseWriteExecutor =
    Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  public static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
            Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DB_NAME
            ).fallbackToDestructiveMigration().build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract ArticleDao articleDao();

  public abstract ExerciseDao exerciseDao();
}
