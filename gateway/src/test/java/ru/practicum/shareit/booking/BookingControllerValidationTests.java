package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.config.HeadersConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerValidationTests {
    @MockBean
    private BookingClient bookingClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private BookingCreateDto bookingCreateDto;
    private BookingUpdateDto bookingUpdateDto;
    private long bookingId;
    private long userId;

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        bookingCreateDto = bookingTestObjects.bookingCreateDto;
        bookingUpdateDto = bookingTestObjects.bookingUpdateDto;
        bookingId = bookingTestObjects.bookingId;
        userId = bookingTestObjects.userId;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveStartInPastBadRequest() throws Exception{
        bookingCreateDto.setStart(LocalDateTime.now().minusHours(5));
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEndInPastBadRequest() throws Exception{
        bookingCreateDto.setEnd(LocalDateTime.now().minusHours(5));
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStartInPastBadRequest() throws Exception{
        bookingCreateDto.setStart(LocalDateTime.now().minusHours(5));
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void updateEndInPastBadRequest() throws Exception{
        bookingCreateDto.setEnd(LocalDateTime.now().minusHours(5));
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
/*
    @Test
    void bookingGetTestById() throws Exception {
        Mockito.when(bookingClient.getBookingById(userId, bookingId))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(bookingCreateDto), HttpStatus.OK));

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(bookingCreateDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreateDto.getStatus().toString())));
    }*/
}
