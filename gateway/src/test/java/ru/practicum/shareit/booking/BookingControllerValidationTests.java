package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.config.HeadersConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookingsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerValidationTests {
    @MockBean
    private BookingsClient bookingClient;

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
    void saveStartInPastBadRequest() throws Exception {
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
    void saveEndInPastBadRequest() throws Exception {
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
    void updateStartInPastBadRequest() throws Exception {
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
    void updateEndInPastBadRequest() throws Exception {
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
    }

    @Test
    void saveBookingTest() throws Exception {
        Mockito.when(bookingClient.createBookingRequest(eq(userId), Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(bookingCreateDto), HttpStatus.OK));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(bookingCreateDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreateDto.getStatus().toString())));
    }

    @Test
    void replyBookingTest() throws Exception {
        bookingUpdateDto.setStatus(BookingStatus.APPROVED);
        Mockito.when(bookingClient.replyToBookingRequest(eq(userId), eq(bookingId), eq(true)))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(bookingUpdateDto), HttpStatus.OK));

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .content(mapper.writeValueAsString(bookingUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingUpdateDto.getStatus().toString())));
    }

    @Test
    void allBookingsGetTest() throws Exception {
        Mockito.when(bookingClient.getAllOwnersBookings(userId, BookingState.ALL))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(List.of(bookingCreateDto, bookingUpdateDto)),
                        HttpStatus.OK));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void allBookerBookingsGetTest() throws Exception {
        Mockito.when(bookingClient.getAllBookersBookings(userId, BookingState.ALL))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(List.of(bookingCreateDto, bookingUpdateDto)),
                        HttpStatus.OK));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void allBookingsNotCorrectStateBadRequestTest() throws Exception {
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .param("state", "WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void allBookerBookingsGetNotCorrectStateBadRequestTest() throws Exception {
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .param("state", "WRONG")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
