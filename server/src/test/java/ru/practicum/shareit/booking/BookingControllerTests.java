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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.config.HeadersConfig;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTests {
    @MockBean
    private BookingService bookingService;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private BookingCreateDto bookingCreateDto;
    private BookingUpdateDto bookingUpdateDto;
    private BookingDto expectedBookingDtoCreated;
    private BookingDto expectedBookingDtoUpdated;
    private BookingDto secondExpectedBookingDto;
    private long bookingId;
    private long userId;

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        bookingCreateDto = bookingTestObjects.bookingCreateDto;
        bookingUpdateDto = bookingTestObjects.bookingUpdateDto;
        expectedBookingDtoCreated = bookingTestObjects.expectedBookingDtoCreated;
        expectedBookingDtoUpdated = bookingTestObjects.expectedBookingDtoUpdated;
        secondExpectedBookingDto = bookingTestObjects.secondExpectedBookingDto;
        bookingId = bookingTestObjects.bookingId;
        userId = bookingTestObjects.userId;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveBookingTest() throws Exception {
        Mockito.when(bookingService.createBookingRequest(eq(userId), Mockito.any()))
                .thenReturn(expectedBookingDtoCreated);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(expectedBookingDtoCreated.getStatus().toString())));
    }

    @Test
    void replyBookingTest() throws Exception {
        Mockito.when(bookingService.replyToBookingRequest(eq(userId), eq(bookingId), eq(true)))
                .thenReturn(expectedBookingDtoUpdated);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .content(mapper.writeValueAsString(bookingUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingDtoUpdated.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(expectedBookingDtoUpdated.getStatus().toString())));
    }

    @Test
    void allBookingsGetTest() throws Exception {
        expectedBookingDtoUpdated.setId(2);
        Mockito.when(bookingService.getAllOwnersBookings(userId, BookingState.ALL))
                .thenReturn(List.of(expectedBookingDtoCreated, secondExpectedBookingDto));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(expectedBookingDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(secondExpectedBookingDto.getId()), Long.class));
    }

    @Test
    void bookingGetTestById() throws Exception {
        Mockito.when(bookingService.getBookingById(userId, bookingId))
                .thenReturn(expectedBookingDtoCreated);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(expectedBookingDtoCreated.getStatus().toString())));
    }
}
