package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJsonTests {
    private final JacksonTester<ItemCreateDto> jacksonCreateTester;
    private final JacksonTester<ItemUpdateDto> jacksonUpdateTester;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private long userId;

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        itemCreateDto = itemTestObjects.itemCreateDto;
        itemUpdateDto = itemTestObjects.itemUpdateDto;
    }

    @Test
    void correctCreateObjectSerialization() throws IOException {
        JsonContent<ItemCreateDto> json = jacksonCreateTester.write(itemCreateDto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(itemCreateDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(itemCreateDto.getDescription());
    }

    @Test
    void correctUpdateObjectSerialization() throws IOException  {
        JsonContent<ItemUpdateDto> json = jacksonUpdateTester.write(itemUpdateDto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(itemUpdateDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(itemUpdateDto.getDescription());
    }

    @Test
    void correctCreateObjectDeserialization() throws IOException  {
        String json = mapper.writeValueAsString(itemCreateDto);

        ItemCreateDto parsedObject = jacksonCreateTester.parseObject(json);

        assertThat(parsedObject.getName()).isEqualTo(itemCreateDto.getName());
        assertThat(parsedObject.getDescription()).isEqualTo(itemCreateDto.getDescription());
        assertThat(parsedObject.getAvailable()).isEqualTo(itemCreateDto.getAvailable());
    }

    @Test
    void correctUpdateObjectDeserialization() throws IOException  {
        String json = mapper.writeValueAsString(itemUpdateDto);

        ItemUpdateDto parsedObject = jacksonUpdateTester.parseObject(json);

        assertThat(parsedObject.getName()).isEqualTo(itemUpdateDto.getName());
        assertThat(parsedObject.getDescription()).isEqualTo(itemUpdateDto.getDescription());
        assertThat(parsedObject.getAvailable()).isEqualTo(itemUpdateDto.getAvailable());
    }
}
