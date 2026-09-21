package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

public interface UserRepository {
    Long add(User user);

    Long update(User user, Long userId);

    User get(Long userId);

    boolean delete(Long userId);
}
