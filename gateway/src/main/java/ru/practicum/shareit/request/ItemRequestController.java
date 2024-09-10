package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                    @RequestBody @Validated ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Creating item request {}, userId={}", itemRequestCreateDto, userId);
        return itemRequestClient.createItemRequest(userId, itemRequestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        log.info("Get all own item requests by user with id={}", userId);
        return itemRequestClient.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOthersItemRequests(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        log.info("Get all others item requests except user with id={}", userId);
        return itemRequestClient.getAllOthersItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                     @Positive @PathVariable(name = "requestId") Long itemRequestId) {
        log.info("Get item requests with id={}, userId={}", itemRequestId, userId);
        return itemRequestClient.getItemRequestById(userId, itemRequestId);
    }
}
