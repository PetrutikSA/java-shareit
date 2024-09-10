package ru.practicum.shareit.item;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.config.HeadersConfig;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @RequestBody @Validated ItemCreateDto itemCreateDto) {
        log.info("Create item {}, userId={}", itemCreateDto, userId);
        return itemClient.createItem(userId, itemCreateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                              @Positive @PathVariable Long itemId) {
        log.info("Get item with id={}, userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(HeadersConfig.USER_ID) Long userId) {
        log.info("Get all items, userId={}", userId);
        return itemClient.getAllItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @RequestBody @Validated ItemUpdateDto itemUpdateDto,
                                             @Positive @PathVariable Long itemId) {
        log.info("Update item {}, userId={}", itemUpdateDto, userId);
        return itemClient.updateItem(userId, itemUpdateDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @Positive @PathVariable Long itemId) {
        log.info("Delete item with id={}, userId={}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                             @RequestParam("text") String text) {
        log.info("Search items with text={} by userId={}", text, userId);
        return itemClient.searchItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HeadersConfig.USER_ID) Long userId,
                                                @Positive @PathVariable Long itemId,
                                                @RequestBody @Validated CommentCreateDto commentCreateDto) {
        log.info("Create comment {} to itemId={}, userId={}", commentCreateDto, itemId, userId);
        return itemClient.createComment(userId, itemId, commentCreateDto);
    }
}
