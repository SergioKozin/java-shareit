package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.get(userRepository.add(UserMapper.toUserEntity(userDto))));
    }

    public UserDto updateUser(UserDto userDto, Long userId) {
        return UserMapper.toUserDto(userRepository.get(userRepository.update(UserMapper.toUserEntity(userDto), userId)));
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.get(userId));
    }

    public boolean deleteUser(Long userId) {
        return userRepository.delete(userId);
    }
}
