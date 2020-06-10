package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMapImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String ADD_OR_EDIT = "/addMeal.jsp";
    private static String LIST_USER = "/meals.jsp";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Integer CALORIES_PER_DAY = 2000;
    private MealDao mealsDao;

    @Override
    public void init() throws ServletException {
        mealsDao = new MealDaoMapImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward = "";
        String action = request.getParameter("action");
        if (action == null) action = "list";

        int mealId;
        switch (action) {
            case ("delete"):
                mealId = Integer.parseInt(request.getParameter("id"));
                mealsDao.delete(mealId);
                log.debug("deleted meal with id= " + mealId);
                response.sendRedirect("meals");
                break;
            case ("edit"):
                forward = ADD_OR_EDIT;
                mealId = Integer.parseInt(request.getParameter("id"));
                Meal meal = mealsDao.findById(mealId);
                request.setAttribute("meal", meal);
                log.debug("go to update meal with id= " + mealId);
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case ("add"):
                forward = ADD_OR_EDIT;
                log.debug("go to add meal");
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            default:
                forward = LIST_USER;
                List<Meal> meals = mealsDao.getAll();
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
                request.setAttribute("mealList", mealsTo);
                request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
                log.debug("get list of meals");
                request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("desc");
        int calories = Integer.parseInt(req.getParameter("calories"));
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("datetime"));
        String mealId = req.getParameter("id");

        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(0, localDateTime, description, calories);
            mealsDao.create(meal);
            log.debug("meal added: " + "{id= "+ meal.getId() +" dateTime=" + meal.getDate() +
                        ", description='" + meal.getDescription() + '\'' +
                        ", calories=" + meal.getCalories() + "}");
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), localDateTime, description, calories);
            mealsDao.update(meal);
            log.debug("meal with id= "+ mealId +" updated ");
        }
        resp.sendRedirect("meals");
    }

}
