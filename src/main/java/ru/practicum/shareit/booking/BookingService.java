package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addBooking(Long itemId, BookingDto bookingDto, long userId);

    BookingDto cancelBooking(long bookingId, long userId);
}
