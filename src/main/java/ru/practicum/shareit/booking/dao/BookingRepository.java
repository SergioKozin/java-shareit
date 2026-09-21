package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.Booking;

public interface BookingRepository {
    Long add(Booking booking);

    Booking get(Long bookingId);

    boolean cancel(Long bookingId);
}
