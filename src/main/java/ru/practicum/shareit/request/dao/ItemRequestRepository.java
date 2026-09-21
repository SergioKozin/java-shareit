package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.ItemRequest;

public interface ItemRequestRepository {
    Long add(ItemRequest itemRequest);

    ItemRequest get(Long itemRequestId);

    boolean delete(Long itemRequestId);
}
