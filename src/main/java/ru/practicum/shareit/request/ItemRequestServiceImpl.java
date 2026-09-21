package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dao.UserRepository;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequestEntity(itemRequestDto);
        itemRequest.setRequestor(userRepository.get(userId));
        return ItemRequestMapper
                .toItemRequestDto(itemRequestRepository.get(itemRequestRepository.add(itemRequest)));
    }

    @Override
    public ItemRequestDto getItemRequest(Long itemRequestId) {
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.get(itemRequestId));
    }

    @Override
    public boolean deleteItemRequest(Long itemRequestId, Long userId) {
        if (!itemRequestRepository.get(itemRequestId).getRequestor().getId().equals(userId))
            throw new ValidationException("Удалить нельзя. Это не ваш запрос!");
        return itemRequestRepository.delete(itemRequestId);
    }
}
