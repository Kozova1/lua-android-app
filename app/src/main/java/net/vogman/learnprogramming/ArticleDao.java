package net.vogman.learnprogramming;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
  @Query("SELECT * FROM `Article`")
  LiveData<List<Article>> loadArticles();

  @Query("UPDATE `Article` SET HasBeenRead = :val WHERE uid = :id")
  void markAsRead(int id, boolean val);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void addArticle(Article article);

  @Query("DELETE FROM `Article` WHERE uid = :id")
  void remove(int id);

  @Query("DELETE FROM `Article`")
  void clear();

  @Insert
  void insertAll(List<Article> articles);
}
