package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        User user = getUserFromRepository(userId);
        Item item = itemMapper.itemCreateToItem(itemCreateDto);
        item.setOwner(user);
        item = itemRepository.save(item);
        log.info("Created new item: {}", item);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(itemId);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        getUserFromRepository(userId); //user existence check
        return itemRepository.findAllByUserId(userId).stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, ItemUpdateDto itemUpdateDto, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(itemId);
        if (item.getOwner().getId() != userId)
            throw new AccessForbiddenException("Item update could be performed only by item's owner");
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
        return itemRepository.findAllByNameOrByDescription(text).stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private Item getItemFromRepository(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class));
    }
}
