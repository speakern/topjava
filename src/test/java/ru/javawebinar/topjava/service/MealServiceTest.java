package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.ls.LSOutput;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static Map<String, Long> statisticMap = new HashMap<>();
    private long startTime;

    @Autowired
    private MealService service;

    @Rule
    public final TestName name = new TestName();

    @Rule
    public final ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            startTime = System.nanoTime();
        }

        @Override
        protected void after() {
            Long endTime = System.nanoTime();
            long elapsedTime = (endTime - startTime);
            log.info("The test {} is over. Time spent: {} ms", name.getMethodName(), TimeUnit.NANOSECONDS.toMillis(elapsedTime));
            statisticMap.put(name.getMethodName(), elapsedTime);
        }
    };

    @ClassRule
    public static final ExternalResource classResource = new ExternalResource() {
        @Override
        protected void after() {
            StringBuilder statMessage = new StringBuilder();
            statisticMap.entrySet().forEach(e ->
                statMessage.append(String.format("\n %-30s %-20s", e.getKey(), Double.toString(e.getValue() * 1.0 / 1_000_000).concat(" ms")))
            );
            log.info(statMessage.toString());
        }
    };

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
    }

    @Test
    public void getAll() throws Exception {
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}