package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestJsonTests {
    private final JacksonTester<ItemRequestCreateDto> jacksonCreateTester;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemRequestCreateDto itemRequestCreateDto;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();
        itemRequestCreateDto = itemRequestTestObjects.itemRequestCreateDto;
    }

    @Test
    void correctCreateObjectSerialization() throws IOException {
        JsonContent<ItemRequestCreateDto> json = jacksonCreateTester.write(itemRequestCreateDto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestCreateDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestCreateDto.getDescription());
    }


    @Test
    void correctCreateObjectDeserialization() throws IOException {
        String json = mapper.writeValueAsString(itemRequestCreateDto);

        ItemRequestCreateDto parsedObject = jacksonCreateTester.parseObject(json);

        assertThat(parsedObject.getName()).isEqualTo(itemRequestCreateDto.getName());
        assertThat(parsedObject.getDescription()).isEqualTo(itemRequestCreateDto.getDescription());
    }
}
