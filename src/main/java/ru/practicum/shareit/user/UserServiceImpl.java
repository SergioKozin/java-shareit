package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUserEntity(userDto)));
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Пользователь не найден: " + userId
                        )
                );

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
