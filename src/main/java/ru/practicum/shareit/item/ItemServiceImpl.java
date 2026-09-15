package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        itemDto.setOwner(userRepository.get(userId));
        return getItem(itemRepository.add(ItemMapper.toItemEntity(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId) {
        return null;
    }

    @Override
    public ItemDto getItem(long itemId) {
        return null;
    }

    @Override
    public Collection<ItemDto> getItemsBySearch(String text) {
        return List.of();
    }
}
