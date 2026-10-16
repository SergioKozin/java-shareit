package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

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
    public ResponseBookingDto addBooking(BookingDto bookingDto, long userId) {
        Booking booking = BookingMapper.toBookingEntity(bookingDto);
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow();
        if (item.getIsAvailable()) {
            booking.setItem(item);
            booking.setStatus(BookingStatus.WAITING);
            return BookingMapper.toResponseBookingDto(bookingRepository.save(booking));
        } else {
            throw new ValidationException("Вещь недоступна для бронирования!");
        }
    }

    @Override
    public ResponseBookingDto approveBooking(Long bookingId, long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            bookingRepository.save(booking);
        } else {
            throw new ForbiddenException("Вы не можете подтвердить бронирование. Это не ваша вещь!");
        }
        return BookingMapper.toResponseBookingDto(bookingRepository.findById(bookingId).orElseThrow());
    }

    @Override
    public ResponseBookingDto getBooking(Long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return BookingMapper.toResponseBookingDto(booking);
        } else {
            throw new ForbiddenException("Это не ваше бронирование!");
        }
    }

    @Override
    public Collection<ResponseBookingDto> getBookings(long userId, BookingState state) {
        return bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getBooker().getId() == userId)
                .map(BookingMapper::toResponseBookingDto)
                .toList();
    }

    @Override
    public Collection<ResponseBookingDto> getOwnerBookings(long userId, BookingState state) {
        List<ResponseBookingDto> ownerBookings = bookingRepository.findAll()
                .stream()
                .filter(booking -> booking.getItem().getOwner().getId() == userId)
                .map(BookingMapper::toResponseBookingDto)
                .toList();
        if (!ownerBookings.isEmpty()) {
            return ownerBookings;
        } else {
            throw new NotFoundException("Нет бронирований для этого пользователя!");
        }
    }

    @Override
    public ResponseBookingDto cancelBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (booking.getBooker().getId().equals(userId)) {
            booking.setStatus(BookingStatus.CANCELED);
            bookingRepository.save(booking);
        }
        return BookingMapper.toResponseBookingDto(bookingRepository.findById(bookingId).orElseThrow());
    }
}
