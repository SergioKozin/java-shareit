package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
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

    @Transactional
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

    @Transactional
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
        List<Booking> result = List.of();
        if (state == BookingState.ALL) {
            result = bookingRepository.findByBookerId(userId, Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.WAITING || state == BookingState.REJECTED) {
            result = bookingRepository.findByBookerIdAndStatus(userId, state.name(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.PAST) {
            result = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.FUTURE) {
            result = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.CURRENT) {
            LocalDateTime now = LocalDateTime.now();
            result = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                    userId,
                    now,
                    now,
                    Sort.by(Sort.Direction.DESC, "start"));
        }
        return result.stream().map(BookingMapper::toResponseBookingDto).toList();
    }

    @Override
    public Collection<ResponseBookingDto> getOwnerBookings(long userId, BookingState state) {
        List<Booking> ownerBookings = List.of();
        if (state == BookingState.ALL) {
            ownerBookings = bookingRepository.findByItemOwnerId(userId,
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.WAITING || state == BookingState.REJECTED) {
            ownerBookings = bookingRepository.findByItemOwnerIdAndStatus(userId, state.name(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.PAST) {
            ownerBookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.FUTURE) {
            ownerBookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(),
                    Sort.by(Sort.Direction.DESC, "start"));
        } else if (state == BookingState.CURRENT) {
            LocalDateTime now = LocalDateTime.now();
            ownerBookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                    userId,
                    now,
                    now,
                    Sort.by(Sort.Direction.DESC, "start"));
        }
        if (!ownerBookings.isEmpty()) {
            return ownerBookings.stream().map(BookingMapper::toResponseBookingDto).toList();
        } else {
            throw new NotFoundException("Нет бронирований для этого пользователя!");
        }
    }

    @Transactional
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
