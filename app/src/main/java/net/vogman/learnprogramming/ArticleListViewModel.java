package net.vogman.learnprogramming;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ArticleListViewModel extends AndroidViewModel {
  private final LiveData<List<Article>> articles;
  private final ArticleRepository repo;

  public ArticleListViewModel(Application application) {
    super(application);
    repo = new ArticleRepository(application);
    articles = repo.getArticles();
  }

  public LiveData<List<Article>> getArticles() {
    return articles;
  }

  public void markAsRead(int id) {
    repo.markRead(id, true);
  }
}
