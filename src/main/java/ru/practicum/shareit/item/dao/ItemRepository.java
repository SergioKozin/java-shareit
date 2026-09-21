package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.Item;

import java.util.Collection;

public interface ItemRepository {
    Long add(Item item);

    Item get(Long itemId);

    boolean delete(Long itemId);

    Collection<Item> getItemsByUser(long userId);

    Collection<Item> getItemsBySearch(String text);
}
