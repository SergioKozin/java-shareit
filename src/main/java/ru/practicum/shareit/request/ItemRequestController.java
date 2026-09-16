package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.addItemRequest(itemRequestDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long itemRequestId) {
        return itemRequestService.getItemRequest(itemRequestId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{itemRequestId}")
    public boolean deleteItemRequest(@PathVariable Long itemRequestId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.deleteItemRequest(itemRequestId, userId);
    }
}
