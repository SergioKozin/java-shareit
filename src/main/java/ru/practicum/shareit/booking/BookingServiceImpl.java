package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingDto addBooking(Long itemId, BookingDto bookingDto, long userId) {
        bookingDto.setItem(itemRepository.get(itemId));
        bookingDto.setBooker(userRepository.get(userId));
        return BookingMapper.toBookingDto(
                bookingRepository.get(bookingRepository.add(BookingMapper.toBookingEntity(bookingDto)))
        );
    }

    @Override
    public BookingDto cancelBooking(long bookingId, long userId) {
        if (bookingRepository.get(bookingId).getBooker().getId() == userId) {
            bookingRepository.cancel(bookingId);
        }
        return BookingMapper.toBookingDto(bookingRepository.get(bookingId));
    }
}
