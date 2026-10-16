package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseBookingDto createBooking(@Valid @RequestBody BookingDto bookingDto,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseBookingDto approveBooking(@Valid @PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestParam boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseBookingDto getBooking(@Valid @PathVariable Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ResponseBookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ResponseBookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getOwnerBookings(userId, state);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseBookingDto cancelBooking(@Valid @PathVariable long bookingId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.cancelBooking(bookingId, userId);
    }
}
