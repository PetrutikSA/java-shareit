package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.config.HeadersConfig;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    public BookingDto createBookingRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                           @RequestBody @Validated BookingCreateDto bookingCreateDto) {
        return bookingService.createBookingRequest(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto replyToBookingRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam boolean approved) {
        return bookingService.replyToBookingRequest(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookersBookings(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllBookersBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnersBookings(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllOwnersBookings(userId, state);
    }
}
