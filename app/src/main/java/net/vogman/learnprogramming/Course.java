package net.vogman.learnprogramming;

import java.util.List;

public class Course {
  public final List<Article> articles;
  public final List<Exercise> exercises;
  public Course(List<Article> articles, List<Exercise> exercises) {
    this.articles = articles;
    this.exercises = exercises;
  }
}
