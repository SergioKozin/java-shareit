package ru.practicum.shareit.booking.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BookingRepositoryInMemory implements BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();

    @Override
    public Long add(Booking booking) {
        booking.setId(getNextId());
        booking.setStatus(BookingStatus.WAITING);
        bookings.put(booking.getId(), booking);
        return booking.getId();
    }

    @Override
    public Booking get(Long bookingId) {
        return bookings.get(bookingId);
    }

    @Override
    public boolean cancel(Long bookingId) {
        bookings.get(bookingId).setStatus(BookingStatus.CANCELED);
        return true;
    }

    private long getNextId() {
        long currentMaxId = bookings.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
