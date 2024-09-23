package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;

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
public class ItemControllerTests {
    @MockBean
    private ItemService itemService;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private ItemDto expectedItemDtoCreated;
    private ItemDto expectedItemDtoUpdated;
    private long itemId;
    private long userId;
    private ItemWithNearestBookingDatesDto expectedItemDtoWithBookingsDatesCreated;
    private ItemWithNearestBookingDatesDto secondExpectedItemDtoWithBookingsDates;

    @BeforeEach
    void setUp() {
        ItemTestObjects itemTestObjects = new ItemTestObjects();
        itemCreateDto = itemTestObjects.itemCreateDto;
        itemUpdateDto = itemTestObjects.itemUpdateDto;
        expectedItemDtoCreated = itemTestObjects.expectedItemDtoCreated;
        expectedItemDtoUpdated = itemTestObjects.expectedItemDtoUpdated;
        itemId = itemTestObjects.itemId;
        userId = itemTestObjects.userId;
        expectedItemDtoWithBookingsDatesCreated = itemTestObjects.expectedItemDtoWithBookingsDatesCreated;
        secondExpectedItemDtoWithBookingsDates = itemTestObjects.secondExpectedItemDtoWithBookingsDates;
    }

    @Test
    void saveItemTest() throws Exception {
        Mockito.when(itemService.createItem(eq(userId), Mockito.any()))
                .thenReturn(expectedItemDtoCreated);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItemDtoCreated.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemDtoCreated.getDescription())));
    }

    @Test
    void updateItemTest() throws Exception {
        Mockito.when(itemService.updateItem(eq(userId), Mockito.any(), eq(itemId)))
                .thenReturn(expectedItemDtoUpdated);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemDtoUpdated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItemDtoUpdated.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemDtoUpdated.getDescription())));
    }

    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(delete("/items/{itemId}", itemId)
                        .header(HeadersConfig.USER_ID, userId))
                .andExpect(status().isOk());
    }

    @Test
    void allItemsGetTest() throws Exception {
        expectedItemDtoUpdated.setId(2);
        Mockito.when(itemService.getAllItems(userId))
                .thenReturn(List.of(expectedItemDtoWithBookingsDatesCreated,
                        secondExpectedItemDtoWithBookingsDates));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(expectedItemDtoWithBookingsDatesCreated.getName())))
                .andExpect(jsonPath("$[1].name", is(secondExpectedItemDtoWithBookingsDates.getName())));
    }

    @Test
    void itemGetTestById() throws Exception {
        Mockito.when(itemService.getItemById(userId, itemId))
                .thenReturn(expectedItemDtoWithBookingsDatesCreated);

        mvc.perform(get("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemDtoWithBookingsDatesCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItemDtoWithBookingsDatesCreated.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemDtoWithBookingsDatesCreated.getDescription())));
    }

}
