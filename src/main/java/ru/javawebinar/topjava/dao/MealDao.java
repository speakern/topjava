package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal findById(Integer id);
    void save(Meal meal);
    void update(Meal meal);
    void delete(Integer id);
    public List<Meal> findAll();
}
