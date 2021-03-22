package net.vogman.learnprogramming;

import java.util.List;

public class ArticleAndExerciseHolder {
  public List<Article> articles;
  public List<Exercise> exercises;
  public ArticleAndExerciseHolder(List<Article> articles, List<Exercise> exercises) {
    this.articles = articles;
    this.exercises = exercises;
  }
}
