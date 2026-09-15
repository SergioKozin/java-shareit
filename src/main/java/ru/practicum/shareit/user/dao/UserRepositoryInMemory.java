package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicatedDataException;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Long add(User user) throws DuplicatedDataException {
        if (isEmailExist(user)) throw new DuplicatedDataException("Этот Email уже используется!");
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public Long update(User user, Long userId) throws DuplicatedDataException {
        if (isEmailExist(user)) throw new DuplicatedDataException("Этот Email уже используется!");
        if (user.getName() != null) users.get(userId).setName(user.getName());
        if (user.getEmail() != null) users.get(userId).setEmail(user.getEmail());
        return userId;
    }

    @Override
    public User get(Long userId) {
        return users.get(userId);
    }

    @Override
    public boolean delete(Long userId) {
        return users.remove(userId) != null;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isEmailExist(User user) {
        return users.entrySet().stream()
                .filter(entry -> Objects.equals(user.getEmail(), entry.getValue().getEmail()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null) != null;
    }
}
