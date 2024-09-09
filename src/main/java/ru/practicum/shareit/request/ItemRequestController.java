package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                            @RequestBody @Validated ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestService.createItemRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllOwnItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        return itemRequestService.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllOthersItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        return itemRequestService.getAllOthersItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @PathVariable(name = "requestId") Long itemRequestId) {
        return itemRequestService.getItemRequestById(userId, itemRequestId);
    }
}
