package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserJsonTests {
    private final JacksonTester<UserCreateDto> jacksonCreateTester;
    private final JacksonTester<UserUpdateDto> jacksonUpdateTester;
    private final ObjectMapper mapper = new ObjectMapper();

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private long userId;

    @BeforeEach
    void setUp() {
        UserTestObjects userTestObjects = new UserTestObjects();
        userCreateDto = userTestObjects.userCreateDto;
        userUpdateDto = userTestObjects.userUpdateDto;
    }

    @Test
    void correctCreateObjectSerialization() throws IOException {
        JsonContent<UserCreateDto> json = jacksonCreateTester.write(userCreateDto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(userCreateDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(userCreateDto.getEmail());
    }

    @Test
    void correctUpdateObjectSerialization() throws IOException  {
        JsonContent<UserUpdateDto> json = jacksonUpdateTester.write(userUpdateDto);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(userUpdateDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(userUpdateDto.getEmail());
    }

    @Test
    void correctCreateObjectDeserialization() throws IOException  {
        String json = mapper.writeValueAsString(userCreateDto);

        UserCreateDto parsedObject = jacksonCreateTester.parseObject(json);

        assertThat(parsedObject.getName()).isEqualTo(userCreateDto.getName());
        assertThat(parsedObject.getEmail()).isEqualTo(userCreateDto.getEmail());
    }

    @Test
    void correctUpdateObjectDeserialization() throws IOException  {
        String json = mapper.writeValueAsString(userUpdateDto);

        UserUpdateDto parsedObject = jacksonUpdateTester.parseObject(json);

        assertThat(parsedObject.getName()).isEqualTo(userUpdateDto.getName());
        assertThat(parsedObject.getEmail()).isEqualTo(userUpdateDto.getEmail());
    }
}
