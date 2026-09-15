package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryInMemory implements ItemRepository{
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Long add(Item item){
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item.getId();
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
