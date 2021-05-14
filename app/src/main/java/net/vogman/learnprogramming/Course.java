package net.vogman.learnprogramming;

import com.google.gson.Gson;

import java.util.List;

public class Course {
    public final List<Article> articles;
    public final List<Exercise> exercises;

    public Course(List<Article> articles, List<Exercise> exercises) {
        this.articles = articles;
        this.exercises = exercises;
    }

    public Course(String asJson) {
        Gson gson = new Gson();
        Course crs = gson.fromJson(asJson, Course.class);
        articles = crs.articles;
        exercises = crs.exercises;
    }
}
