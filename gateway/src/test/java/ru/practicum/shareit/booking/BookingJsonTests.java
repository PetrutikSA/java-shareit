package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingJsonTests {

    private final JacksonTester<BookingCreateDto> jacksonCreateTester;
    private final JacksonTester<BookingUpdateDto> jacksonUpdateTester;
    private final ObjectMapper mapper = new ObjectMapper();


    private BookingCreateDto bookingCreateDto;
    private BookingUpdateDto bookingUpdateDto;
    private long userId;

    @BeforeEach
    void setUp() {
        BookingTestObjects bookingTestObjects = new BookingTestObjects();
        bookingCreateDto = bookingTestObjects.bookingCreateDto;
        bookingUpdateDto = bookingTestObjects.bookingUpdateDto;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void correctCreateObjectSerialization() throws IOException {
        JsonContent<BookingCreateDto> json = jacksonCreateTester.write(bookingCreateDto);
        assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo(bookingCreateDto.getStatus().toString());
    }

    @Test
    void correctUpdateObjectSerialization() throws IOException {
        JsonContent<BookingUpdateDto> json = jacksonUpdateTester.write(bookingUpdateDto);
        assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo(bookingUpdateDto.getStatus().toString());
    }

    @Test
    void correctCreateObjectDeserialization() throws IOException {
        String json = mapper.writeValueAsString(bookingCreateDto);

        BookingCreateDto parsedObject = jacksonCreateTester.parseObject(json);

        assertThat(parsedObject.getStart()).isEqualTo(bookingCreateDto.getStart());
        assertThat(parsedObject.getEnd()).isEqualTo(bookingCreateDto.getEnd());
        assertThat(parsedObject.getStatus()).isEqualTo(bookingCreateDto.getStatus());
    }

    @Test
    void correctUpdateObjectDeserialization() throws IOException {
        String json = mapper.writeValueAsString(bookingUpdateDto);

        BookingUpdateDto parsedObject = jacksonUpdateTester.parseObject(json);

        assertThat(parsedObject.getStart()).isEqualTo(bookingUpdateDto.getStart());
        assertThat(parsedObject.getEnd()).isEqualTo(bookingUpdateDto.getEnd());
        assertThat(parsedObject.getStatus()).isEqualTo(bookingUpdateDto.getStatus());
    }
}
