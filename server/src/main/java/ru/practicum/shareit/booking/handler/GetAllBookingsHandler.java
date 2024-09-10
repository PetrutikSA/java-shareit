package ru.practicum.shareit.booking.handler;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.ArrayList;
import java.util.List;

public abstract class GetAllBookingsHandler {
    private GetAllBookingsHandler next;

    public static GetAllBookingsHandler link(GetAllBookingsHandler first, GetAllBookingsHandler... chain) {
        GetAllBookingsHandler head = first;
        for (GetAllBookingsHandler nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract List<Booking> handle(Long userId, BookingState state, BookingRepository bookingRepository);

    protected List<Booking> handleNext(Long userId, BookingState state, BookingRepository bookingRepository) {
        if (next == null) {
            return new ArrayList<>(); //Если вариант не один обработчик не выполнен возвращает пустой List
        }
        return next.handle(userId, state, bookingRepository);
    }
}
