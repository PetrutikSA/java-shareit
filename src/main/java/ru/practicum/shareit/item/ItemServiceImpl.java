package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingDatesDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.BadRequestException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        User user = getUserFromRepository(userId);
        Item item = itemMapper.itemCreateToItem(itemCreateDto);
        item.setOwner(user);
        Long itemRequestId = itemCreateDto.getRequestId();
        if (itemRequestId != null && itemRequestId != 0) {
            ItemRequest itemRequest = getItemRequestFromRepository(itemRequestId);
            List<ItemRequest> itemRequests = new ArrayList<>();
            itemRequests.add(itemRequest);
            item.setItemRequests(itemRequests);
        }
        item = itemRepository.save(item);
        log.info("Created new item: {}", item);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemWithNearestBookingDatesDto getItemById(Long userId, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(itemId);
        Booking bookingNext = bookingRepository
                .findAllByItemOwnerIdNextItemBookingFromDate(userId, Set.of(item.getId()), LocalDateTime.now())
                .stream()
                .findAny()
                .orElse(null);
        Booking bookingLast = bookingRepository
                .findAllByItemOwnerIdLastItemBookingFromDate(userId, Set.of(item.getId()), LocalDateTime.now())
                .stream()
                .findAny()
                .orElse(null);
        ItemWithNearestBookingDatesDto itemWithNearestBookingDatesDto = itemMapper.itemToItemWithNearestDatesDto(item);
        itemWithNearestBookingDatesDto.setLastBooking(bookingMapper.bookingToBookingOnlyDatesDto(bookingLast));
        itemWithNearestBookingDatesDto.setNextBooking(bookingMapper.bookingToBookingOnlyDatesDto(bookingNext));
        return itemWithNearestBookingDatesDto;
    }

    @Override
    public List<ItemWithNearestBookingDatesDto> getAllItems(Long userId) {
        getUserFromRepository(userId); //user existence check
        Map<Long, ItemWithNearestBookingDatesDto> items = itemRepository.findAllByOwnerId(userId).stream()
                .map(itemMapper::itemToItemWithNearestDatesDto)
                .collect(Collectors.toMap(ItemWithNearestBookingDatesDto::getId, Function.identity()));
        List<Booking> bookingNextAll = bookingRepository
                .findAllByItemOwnerIdNextItemBookingFromDate(userId, items.keySet(), LocalDateTime.now());
        List<Booking> bookingLastAll = bookingRepository
                .findAllByItemOwnerIdLastItemBookingFromDate(userId, items.keySet(), LocalDateTime.now());
        for (Booking booking : bookingNextAll) {
            ItemWithNearestBookingDatesDto item = items.get(booking.getItem().getId());
            item.setNextBooking(bookingMapper.bookingToBookingOnlyDatesDto(booking));
        }
        for (Booking booking : bookingLastAll) {
            ItemWithNearestBookingDatesDto item = items.get(booking.getItem().getId());
            item.setLastBooking(bookingMapper.bookingToBookingOnlyDatesDto(booking));
        }
        return items.values().stream().toList();
    }

    @Override
    public ItemDto updateItem(Long userId, ItemUpdateDto itemUpdateDto, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(itemId);
        if (item.getOwner().getId() != userId)
            throw new AccessForbiddenException("Item update could be performed only by item's owner");
        Long itemRequestId = itemUpdateDto.getRequestId();
        if (itemRequestId != null && itemRequestId != 0) {
            ItemRequest itemRequest = getItemRequestFromRepository(itemRequestId);
            List<ItemRequest> itemRequests = item.getItemRequests();
            if (itemRequests == null)
                itemRequests = new ArrayList<>();
            itemRequests.add(itemRequest);
            item.setItemRequests(itemRequests);
        }
        itemMapper.itemUpdateToItem(itemUpdateDto, item);
        item = itemRepository.save(item);
        log.info("Updated item: {}", item);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(itemId);
        if (item.getOwner().getId() != userId)
            throw new AccessForbiddenException("Item delete could be performed only by item's owner");
        itemRepository.deleteById(itemId);
        log.info("Deleted item: {}", item);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank() || text.isEmpty()) return new ArrayList<>();
        return itemRepository.findAllByNameOrDescription(text).stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        User author = getUserFromRepository(userId);
        Item item = getItemFromRepository(itemId);
        bookingRepository
                .findAllByBookerIdAndItemIdAndEndLessThan(userId, itemId, LocalDateTime.now()).stream().findAny()
                .orElseThrow(() -> new BadRequestException("Comment add could be performed only by item's bookers"));
        Comment comment = commentMapper.commentCreateDtoToComment(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private Item getItemFromRepository(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class));
    }

    private ItemRequest getItemRequestFromRepository(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(itemRequestId, ItemRequest.class));
    }
}
