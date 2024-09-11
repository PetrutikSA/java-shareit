package ru.practicum.shareit.request;

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
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTests {
    @MockBean
    private ItemRequestService itemRequestService;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestDto expectedItemRequestDtoCreated;
    private long itemRequestId;
    private long userId;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();
        itemRequestCreateDto = itemRequestTestObjects.itemRequestCreateDto;
        expectedItemRequestDtoCreated = itemRequestTestObjects.expectedItemRequestDtoCreated;
        itemRequestId = itemRequestTestObjects.itemRequestId;
        userId = itemRequestTestObjects.userId;
    }

    @Test
    void saveItemRequestTest() throws Exception {
        Mockito.when(itemRequestService.createItemRequest(eq(userId), Mockito.any()))
                .thenReturn(expectedItemRequestDtoCreated);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemRequestDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItemRequestDtoCreated.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemRequestDtoCreated.getDescription())));
    }

    @Test
    void allItemsGetTest() throws Exception {
        expectedItemRequestDtoCreated.setId(2);
        Mockito.when(itemRequestService.getAllOwnItemRequests(userId))
                .thenReturn(List.of(expectedItemRequestDtoCreated));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(expectedItemRequestDtoCreated.getName())))
                .andExpect(jsonPath("$[0].description", is(expectedItemRequestDtoCreated.getDescription())));
    }

    @Test
    void itemGetTestById() throws Exception {
        Mockito.when(itemRequestService.getItemRequestById(userId, itemRequestId))
                .thenReturn(expectedItemRequestDtoCreated);

        mvc.perform(get("/requests/{itemRequestsId}", itemRequestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemRequestDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(expectedItemRequestDtoCreated.getName())))
                .andExpect(jsonPath("$.description", is(expectedItemRequestDtoCreated.getDescription())));
    }
}
