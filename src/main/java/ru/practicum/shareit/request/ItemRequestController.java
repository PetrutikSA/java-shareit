package ru.practicum.shareit.request;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUpdateDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                            @RequestBody @Validated ItemRequestCreateDto itemRequestCreateDto) {
        return null;
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateItemRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                            @PathVariable(name = "requestId") Long itemRequestId,
                                            @RequestBody ItemRequestUpdateDto itemRequestUpdateDto) {
        return null;
    }

    //Получить список своих запросов вместе с данными об ответах на них.
    //Запросы должны возвращаться отсортированными от более новых к более старым.
    @GetMapping
    public List<ItemRequestDto> getAllOwnItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        return null;
    }

    //получить список запросов, созданных другими пользователями.
    @GetMapping("/all")
    public List<ItemRequestDto> getAllOthersItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        return null;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @PathVariable(name = "requestId") Long itemRequestId) {
        return null;
    }
}
