package ru.javawebinar.topjava.services;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMapImpl;
import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public class MealService {

    private MealDao mealsDao = new MealDaoMapImpl();

    public MealService() {
    }

    public Meal find(Integer id) {
        return mealsDao.findById(id);
    }

    public void save(Meal meal) {
        mealsDao.save(meal);
    }

    public void delete(Integer id) {
        mealsDao.delete(id);
    }

    public void update(Meal meal) {
        mealsDao.update(meal);
    }

    public List<Meal> findAll() {
        return mealsDao.findAll();
    }

}
