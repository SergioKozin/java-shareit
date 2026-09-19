package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, long userId);

    BookingDto getBooking(Long bookingId, long userId);

    BookingDto cancelBooking(long bookingId, long userId);
}
