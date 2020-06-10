package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal findById(Integer id);
    Meal create(Meal meal);
    Meal update(Meal meal);
    void delete(Integer id);
    List<Meal> getAll();
}
