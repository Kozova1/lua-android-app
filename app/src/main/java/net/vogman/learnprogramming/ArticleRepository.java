package net.vogman.learnprogramming;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;

public class ArticleRepository {
  private final LiveData<List<Article>> articles;
  private final ArticleDao dao;

  ArticleRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    dao = db.articleDao();
    articles = dao.loadArticles();
  }

  LiveData<List<Article>> getArticles() {
    return articles;
  }

  void markRead(int id, boolean val) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      dao.markAsRead(id, val);
    });
  }

  void addArticle(Article article) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      dao.addArticle(article);
    });
  }
}