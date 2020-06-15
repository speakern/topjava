package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        //User(Integer id, String name, String email, String password, Role role, Role... roles)
        save(new User(null, "user1", "user2@mail.ru", "pswd", Role.USER));
        save(new User(null, "user1", "user1@mail.ru", "pswd", Role.USER));
        save(new User(null, "user3", "user3@mail.ru", "pswd", Role.USER));
        save(new User(null, "admin", "admin@mail.ru", "pswd", Role.ADMIN));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted((u1, u2) -> u1.getName() != u2.getName() ? u1.getEmail().compareTo(u2.getEmail()) :
                        u1.getEmail().compareTo(u2.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> users = repository.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .collect(Collectors.toList());
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
