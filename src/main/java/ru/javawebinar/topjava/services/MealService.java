package ru.javawebinar.topjava.services;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMapImpl;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealService {

    private MealDao mealsDao = new MealDaoMapImpl();

    public MealService() {
    }

    public Meal findMeal(int id) {
        return mealsDao.findById(id);
    }

    public void saveMeal(Meal meal) {
        mealsDao.save(meal);
    }

    public void deleteMeal(Meal meal) {
        mealsDao.delete(meal);
    }

    public void updateMeal(Meal meal) {
        mealsDao.update(meal);
    }

    public List<Meal> findAllMeals() {
        return mealsDao.findAll();
    }

}
