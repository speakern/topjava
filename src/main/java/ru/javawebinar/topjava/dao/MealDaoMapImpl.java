package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMapImpl implements MealDao {
    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public MealDaoMapImpl() {
        create(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(2,  LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(3,  LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(4,  LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(5,  LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(6,  LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(7,  LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private int getNextId() {
        return counter.addAndGet(1);
    }

    @Override
    public Meal findById(Integer id) {
        return mealMap.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        Meal newMeal = new Meal(getNextId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        mealMap.put(counter.get(), newMeal);
        return newMeal;
    }

    @Override
    public Meal update(Meal meal) {
        if (mealMap.replace(meal.getId(), meal) == null) {
            return null;
        } else {
            return meal;
        }
    }

    @Override
    public void delete(Integer id) {
        mealMap.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }
}
