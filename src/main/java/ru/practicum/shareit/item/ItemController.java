package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody UpdateItemDto updateItemDto) {
        return itemService.updateItem(itemId, userId, updateItemDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@Valid @PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{itemId}")
    public boolean getItemById(@Valid @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.deleteItem(itemId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearch(@RequestParam String text) {
        return itemService.getItemsBySearch(text);
    }
}
