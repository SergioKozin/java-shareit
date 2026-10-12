package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.toItemEntity(itemDto);
        item.setOwner(userRepository.findById(userId).orElseThrow());
        item.setRequest(itemRequestRepository.get(itemDto.getRequest()));
        return ItemMapper.toItemDto(itemRepository.findById(itemRepository.save(item).getId()).orElseThrow());
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, UpdateItemDto updateItemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        if (!item.getOwner().getId().equals(userId))
            throw new ForbiddenException("Изменить нельзя. Это не ваша вещь!");
        if (updateItemDto.getDescription() != null) item.setDescription(updateItemDto.getDescription());
        if (updateItemDto.getName() != null) item.setName(updateItemDto.getName());
        if (updateItemDto.getAvailable() != null) item.setIsAvailable(updateItemDto.getAvailable());
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow());
    }

    @Override
    public boolean deleteItem(long itemId, long userId) {
        if (!itemRepository.findById(itemId).orElseThrow().getOwner().getId().equals(userId))
            throw new ForbiddenException("Удалить нельзя. Это не ваша вещь!");
        itemRepository.deleteById(itemId);
        return true;
    }

    @Override
    public Collection<ItemDto> getItemsByUser(long userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsBySearch(String text) {
        if (text.isEmpty()) return new ArrayList<ItemDto>();
        return itemRepository.findAllBySearch(text)
                .stream()
                .filter(Item::getIsAvailable)
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
