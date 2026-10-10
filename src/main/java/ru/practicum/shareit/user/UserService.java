package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public interface UserService {
    public UserDto addUser(UserDto userDto);

    public UserDto updateUser(UserDto userDto, Long userId);

    public UserDto getUser(Long userId);

    public boolean deleteUser(Long userId);
}
