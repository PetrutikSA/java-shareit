package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

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
public class ItemRequestControllerValidationTests {
    @MockBean
    private ItemRequestClient itemRequestClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private ItemRequestCreateDto itemRequestCreateDto;
    private long userId;
    private long itemRequestId;

    @BeforeEach
    void setUp() {
        ItemRequestTestObjects itemRequestTestObjects = new ItemRequestTestObjects();
        itemRequestCreateDto = itemRequestTestObjects.itemRequestCreateDto;
        userId = 1L;
        itemRequestId = 1L;
    }

    @Test
    void saveItemRequestTest() throws Exception {
        Mockito.when(itemRequestClient.createItemRequest(eq(userId), Mockito.any()))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(itemRequestCreateDto), HttpStatus.OK));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemRequestCreateDto.getName())))
                .andExpect(jsonPath("$.description", is(itemRequestCreateDto.getDescription())));
    }

    @Test
    void saveItemRequestNullDescriptionBadRequest() throws Exception {
        itemRequestCreateDto.setDescription("         ");
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveItemRequestBlankDescriptionBadRequest() throws Exception {
        itemRequestCreateDto.setDescription(null);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void allItemsRequestsGetTest() throws Exception {
        Mockito.when(itemRequestClient.getAllOwnItemRequests(userId))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(List.of(itemRequestCreateDto)),
                        HttpStatus.OK));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(itemRequestCreateDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemRequestCreateDto.getDescription())));
    }

    @Test
    void itemRequestsGetTestById() throws Exception {
        Mockito.when(itemRequestClient.getItemRequestById(userId, itemRequestId))
                .thenReturn(new ResponseEntity<>(mapper.writeValueAsString(itemRequestCreateDto), HttpStatus.OK));

        mvc.perform(get("/requests/{itemRequestsId}", itemRequestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HeadersConfig.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemRequestCreateDto.getName())))
                .andExpect(jsonPath("$.description", is(itemRequestCreateDto.getDescription())));
    }
}
