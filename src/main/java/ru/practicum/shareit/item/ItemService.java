package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, long userId, UpdateItemDto updateItemDto);

    ItemDto getItem(long itemId);

    boolean deleteItem(long itemId, long userId);

    Collection<ItemDto> getItemsByUser(long userId);

    Collection<ItemDto> getItemsBySearch(String text);
}
