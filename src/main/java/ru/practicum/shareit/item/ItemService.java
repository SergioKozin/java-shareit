package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long userId);
}
