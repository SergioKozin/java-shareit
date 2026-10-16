package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.Collection;

public interface BookingService {
    ResponseBookingDto addBooking(BookingDto bookingDto, long userId);

    ResponseBookingDto getBooking(Long bookingId, long userId);

    ResponseBookingDto cancelBooking(long bookingId, long userId);

    ResponseBookingDto approveBooking(@Valid Long bookingId, long userId, @Valid boolean approved);

    Collection<ResponseBookingDto> getBookings(long userId, BookingState state);

    Collection<ResponseBookingDto> getOwnerBookings(long userId, BookingState state);
}
