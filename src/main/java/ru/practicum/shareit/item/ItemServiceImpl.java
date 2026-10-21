package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           ItemRequestRepository itemRequestRepository, CommentRepository commentRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.toItemEntity(itemDto);
        item.setOwner(userRepository.findById(userId).orElseThrow());
        if (itemDto.getRequest() != null) {
            item.setRequest(
                    itemRequestRepository.findById(itemDto.getRequest())
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "Запрос не найден: " + itemDto.getRequest()))
            );
        } else {
            item.setRequest(null);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
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
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow());
        List<Booking> nextBookings = bookingRepository.findByItemIdAndStartIsAfter(itemId, LocalDateTime.now(),
                Sort.by(Sort.Direction.ASC, "start"));
        if (!nextBookings.isEmpty()) itemDto.setNextBooking(nextBookings.getFirst());
        itemDto.setComments(commentRepository.findAllByItemId(itemId));
        return itemDto;
    }

    @Transactional
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
                .peek(itemDto -> {
                    List<Booking> lastBookings = bookingRepository
                            .findByItemIdAndEndIsBefore(itemDto.getId(), LocalDateTime.now(),
                                    Sort.by(Sort.Direction.DESC, "end"));
                    if (!lastBookings.isEmpty()) itemDto.setLastBooking(lastBookings.getFirst());
                    List<Booking> nextBookings = bookingRepository
                            .findByItemIdAndStartIsAfter(itemDto.getId(), LocalDateTime.now(),
                                    Sort.by(Sort.Direction.ASC, "start"));
                    if (!nextBookings.isEmpty()) itemDto.setNextBooking(nextBookings.getFirst());
                    itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId()));
                })
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsBySearch(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemRepository.findAllBySearch(text)
                .stream()
                .filter(Item::getIsAvailable)
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) {
        Comment savedcomment;
        if (!bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "end"))
                .stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .toList().isEmpty()) {
            commentDto.setCreated(LocalDateTime.now());
            commentDto.setItem(itemRepository.findById(itemId).orElseThrow());
            Comment comment = CommentMapper.toCommentEntity(commentDto);
            comment.setAuthor(userRepository.findById(userId).orElseThrow());
            savedcomment = commentRepository.save(comment);
        } else {
            throw new ValidationException("Оставить отзыв нельзя. Эту вещь не брали в аренду!");
        }
        return CommentMapper.toCommentDto(savedcomment);
    }
}
