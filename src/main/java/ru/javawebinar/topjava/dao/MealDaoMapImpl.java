package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;

public class MealDaoMapImpl implements MealDao {

    private Map mealMap = MealsUtil.mealMap;

    @Override
    public Meal findById(int id) {
        return null;
    }

    @Override
    public void save(Meal meal) {

    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public void delete(Meal meal) {

    }

    @Override
    public List<Meal> findAll() {
        return null;
    }
}
