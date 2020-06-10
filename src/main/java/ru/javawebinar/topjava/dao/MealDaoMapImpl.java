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

    private static AtomicInteger counter = new AtomicInteger(0);

    public MealDaoMapImpl() {
        save(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(2,  LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        save(new Meal(3,  LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        save(new Meal(4,  LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        save(new Meal(5,  LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(6,  LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        save(new Meal(7,  LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public static int getNextID() {
        return counter.addAndGet(1);
    }

    @Override
    public Meal findById(Integer id) {
        return mealMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        Meal newMeal = new Meal(getNextID(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return mealMap.put(counter.get(), newMeal);
    }

    @Override
    public Meal update(Meal meal) {
        return mealMap.put(meal.getId(), meal);
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
