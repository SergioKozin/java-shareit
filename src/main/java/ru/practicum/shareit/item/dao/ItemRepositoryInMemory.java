package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Long add(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item.getId();
    }

    @Override
    public Item get(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public boolean delete(Long itemId) {
        return items.remove(itemId) != null;
    }

    @Override
    public Collection<Item> getItemsByUser(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
    }

    @Override
    public Collection<Item> getItemsBySearch(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getAvailable() == true)
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && !text.isEmpty())
                .toList();
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
