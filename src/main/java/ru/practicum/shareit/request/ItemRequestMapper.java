package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
    }

    public ItemRequest toItemRequestEntity(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequestor(),
                itemRequestDto.getCreated()
        );
    }
}
