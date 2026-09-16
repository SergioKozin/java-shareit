package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        itemDto.setOwner(userRepository.get(userId));
        Item item = ItemMapper.toItemEntity(itemDto);
        item.setRequest(itemRequestRepository.get(itemDto.getRequest()));
        if (itemDto.getOwner() == null) throw new NotFoundException("Пользователь не найден!");
        return ItemMapper.toItemDto(itemRepository.get(itemRepository.add(item)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, UpdateItemDto updateItemDto) {
        Item item = itemRepository.get(itemId);
        if (!item.getOwner().getId().equals(userId))
            throw new ForbiddenException("Изменить нельзя. Это не ваша вещь!");
        if (updateItemDto.getDescription() != null) item.setDescription(updateItemDto.getDescription());
        if (updateItemDto.getName() != null) item.setName(updateItemDto.getName());
        if (updateItemDto.getAvailable() != null) item.setAvailable(updateItemDto.getAvailable());
        return ItemMapper.toItemDto(itemRepository.get(itemId));
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemRepository.get(itemId));
    }

    @Override
    public boolean deleteItem(long itemId, long userId) {
        if (!itemRepository.get(itemId).getOwner().getId().equals(userId))
            throw new ForbiddenException("Удалить нельзя. Это не ваша вещь!");
        return itemRepository.delete(itemId);
    }

    @Override
    public Collection<ItemDto> getItemsByUser(long userId) {
        return List.of();
    }

    // поиск текста в описание и названии вещи
    @Override
    public Collection<ItemDto> getItemsBySearch(String text) {
        return List.of();
    }
}
