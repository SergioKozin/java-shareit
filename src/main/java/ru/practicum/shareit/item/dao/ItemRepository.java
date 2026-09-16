package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.Item;

public interface ItemRepository {
    Long add(Item item);

    Item get(Long itemId);

    boolean delete(Long itemId);
}
