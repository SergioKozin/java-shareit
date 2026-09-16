package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto item, Long userId);

    ItemRequestDto getItemRequest(Long itemRequestId);

    boolean deleteItemRequest(Long itemRequestId, Long userId);
}
