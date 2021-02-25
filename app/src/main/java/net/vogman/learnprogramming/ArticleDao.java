package net.vogman.learnprogramming;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
  @Query("SELECT * FROM `Article`")
  LiveData<List<Article>> loadArticles();

  @Query("UPDATE `Article` SET HasBeenRead = :val WHERE uid = :id")
  void markAsRead(int id, boolean val);

  @Insert
  void addArticle(Article article);
}
