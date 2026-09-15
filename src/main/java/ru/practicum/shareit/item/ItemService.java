package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, long userId);

    ItemDto getItem(long itemId);

    Collection<ItemDto> getItemsBySearch(String text);
}
