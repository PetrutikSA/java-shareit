package ru.practicum.shareit.review.model;

import lombok.Data;

@Data
public class Review {
    private long id;
    private long itemId;
    private long bookingId;
    private String text;
}
