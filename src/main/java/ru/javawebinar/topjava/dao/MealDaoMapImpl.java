package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealDaoMapImpl implements MealDao {

    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    @Override
    public Meal findById(Integer id) {
        return mealMap.get(id);
    }

    @Override
    public void save(Meal meal) {
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        mealMap.remove(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList(mealMap.values());
    }
}
