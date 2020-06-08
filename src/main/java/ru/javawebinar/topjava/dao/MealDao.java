package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal findById(int id);
    void save(Meal meal);
    void update(Meal meal);
    void delete(Meal meal);
    public List<Meal> findAll();
}
