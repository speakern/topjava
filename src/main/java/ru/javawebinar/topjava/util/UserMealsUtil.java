package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDays = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDays.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> resultList = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean excess = caloriesPerDays.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
                resultList.add(userMealWithExcess);
            }
        }
        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDays = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return meals.stream().filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map((meal) -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesPerDays.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        class WithExcessCollector implements Collector<UserMeal, Map.Entry<List<UserMeal>, List<UserMeal>>, List<UserMealWithExcess>> {
            private Integer caloriesPerDay;
            private LocalTime startTime;
            private LocalTime endTime;
            private Map<LocalDate, Integer> caloriesPerDays = new HashMap<LocalDate, Integer>();

            public WithExcessCollector(LocalTime startTime, LocalTime endTime, Integer caloriesPerDay) {
                this.caloriesPerDay = caloriesPerDay;
                this.startTime = startTime;
                this.endTime = endTime;
            }

            @Override
            public Supplier<Map.Entry<List<UserMeal>, List<UserMeal>>> supplier() {
                return () -> new AbstractMap.SimpleImmutableEntry<>(
                        new ArrayList<UserMeal>(),
                        new ArrayList<UserMeal>()
                );
            }

            @Override
            public BiConsumer<Map.Entry<List<UserMeal>, List<UserMeal>>, UserMeal> accumulator() {
                return (c, e) -> {
                    caloriesPerDays.merge(e.getDateTime().toLocalDate(), e.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime)) {
                        if (caloriesPerDays.get(e.getDateTime().toLocalDate()) > caloriesPerDay) {
                            c.getValue().add(e);
                        } else {
                            c.getKey().add(e);
                        }
                    }
                };
            }

            @Override
            public BinaryOperator<Map.Entry<List<UserMeal>, List<UserMeal>>> combiner() {
                return (c1, c2) -> {
                    c1.getKey().addAll(c2.getKey());
                    c1.getValue().addAll(c2.getValue());
                    return c1;
                };
            }

            @Override
            public Function<Map.Entry<List<UserMeal>, List<UserMeal>>, List<UserMealWithExcess>> finisher() {
                return c -> {
                    List<UserMealWithExcess> resultList = new ArrayList<>();

                    c.getKey().forEach(meal -> {
                        resultList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                                caloriesPerDays.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
                    });
                    c.getValue().forEach(meal -> {
                        resultList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                                true));
                    });
                    return resultList;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.UNORDERED);
            }
        }

        return meals.stream().collect(new WithExcessCollector(startTime, endTime, caloriesPerDay));
    }
}
