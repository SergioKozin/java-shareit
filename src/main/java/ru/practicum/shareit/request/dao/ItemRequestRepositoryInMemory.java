package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.HashMap;

@Repository
public class ItemRequestRepositoryInMemory implements ItemRequestRepository {
    private final HashMap<Long, ItemRequest> itemRequests = new HashMap<>();

    @Override
    public Long add(ItemRequest itemRequest) {
        itemRequest.setId(getNextId());
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest.getId();
    }

    @Override
    public ItemRequest get(Long itemRequestId) {
        return itemRequests.get(itemRequestId);
    }

    @Override
    public boolean delete(Long itemRequestId) {
        return itemRequests.remove(itemRequestId) != null;
    }

    private long getNextId() {
        long currentMaxId = itemRequests.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
