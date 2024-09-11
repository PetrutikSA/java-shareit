package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerValidationTests {
    @MockBean
    private ItemClient itemClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private long userId;
    private long itemId;

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        itemCreateDto = itemTestObjects.itemCreateDto;
        itemUpdateDto = itemTestObjects.itemUpdateDto;
        userId = 1L;
        itemId = 1L;
    }

    @Test
    void saveItemTest() throws Exception {
        Mockito.when(itemClient.createItem(eq(userId), Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(itemCreateDto), HttpStatus.OK));

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemCreateDto.getName())))
                .andExpect(jsonPath("$.description", is(itemCreateDto.getDescription())));
    }

    @Test
    void updateItemTest() throws Exception {
        Mockito.when(itemClient.updateItem(eq(userId), Mockito.any(), eq(itemId)))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(itemUpdateDto), HttpStatus.OK));

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemUpdateDto.getName())))
                .andExpect(jsonPath("$.description", is(itemUpdateDto.getDescription())));
    }

    @Test
    void saveWithBlankNameBadRequest() throws Exception {
        itemCreateDto.setName("       ");
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveWithNullDescriptionBadRequest() throws Exception {
        itemCreateDto.setName(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithBlankNameBadRequest() throws Exception {
        itemUpdateDto.setName("         ");
        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithBlankDescriptionBadRequest() throws Exception {
        itemUpdateDto.setDescription("         ");
        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCorrectWithNullNameAndDescriptionBadRequest() throws Exception {
        itemUpdateDto.setName(null);
        itemUpdateDto.setDescription(null);
        Mockito.when(itemClient.updateItem(eq(userId), Mockito.any(), eq(itemId)))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(itemCreateDto), HttpStatus.OK));

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemCreateDto.getName())))
                .andExpect(jsonPath("$.description", is(itemCreateDto.getDescription())));
    }

    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(delete("/items/{itemId}", itemId)
                        .header(HeadersConfig.USER_ID, userId))
                .andExpect(status().isOk());
    }

    @Test
    void allItemsGetTest() throws Exception {
        Mockito.when(itemClient.getAllItems(userId))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(List.of(itemCreateDto, itemUpdateDto)),
                        HttpStatus.OK));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(itemCreateDto.getName())))
                .andExpect(jsonPath("$[1].name", is(itemUpdateDto.getName())));
    }
}
