package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.services.MealService;
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
    private MealService mealService = MealsUtil.mealService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");
        if (action == null) action = "list";

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            mealService.delete(mealId);
            response.sendRedirect("meals");
        } else if (action.equalsIgnoreCase("edit")) {
            forward = ADD_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealService.find(mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("dateTime", meal.getDateTime().format(dateTimeFormatter));
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            forward = LIST_USER;
            List<Meal> meals = mealService.findAll();
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("mealList", mealsTo);
            request.setAttribute("dateTimeFormatter", dateTimeFormatter);
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("desc");
        Integer calories = Integer.parseInt(req.getParameter("calories"));
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("datetime"), dateTimeFormatter);
        String mealId = req.getParameter("id");

        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(MealsUtil.getNextID(), localDateTime, description, calories);
            mealService.save(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), localDateTime, description, calories);
            mealService.update(meal);
        }
        resp.sendRedirect("meals");
    }

}
