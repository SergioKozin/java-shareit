package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{itemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@Valid @PathVariable Long itemId,
                                    @Valid @RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(itemId, bookingDto, userId);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto cancelBooking(@Valid @PathVariable long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.cancelBooking(bookingId, userId);
    }
}
