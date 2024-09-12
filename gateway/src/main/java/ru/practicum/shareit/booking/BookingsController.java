package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.config.HeadersConfig;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingsController {
    private final BookingsClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBookingRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                       @RequestBody @Validated BookingCreateDto bookingCreateDto) {
        log.info("Creating booking {}, userId={}", bookingCreateDto, userId);
        return bookingClient.createBookingRequest(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> replyToBookingRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                        @PathVariable Long bookingId,
                                                        @RequestParam boolean approved) {
        log.info("Replying to bookingId={}, approved={}, userId={}", bookingId, approved, userId);
        return bookingClient.replyToBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookersBookings(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking made by booker with id={}, with state {}", userId, stateParam);
        return bookingClient.getAllBookersBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnersBookings(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking made by item owner with id={}, with state {}", userId, stateParam);
        return bookingClient.getAllOwnersBookings(userId, state);
    }
}