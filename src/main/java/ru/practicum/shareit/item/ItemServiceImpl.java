package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.InternalServerException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        User user = getUserFromRepository(userId);
        Item item = ItemMapper.MAPPER.itemCreateToItem(itemCreateDto);
        item.setOwner(user);
        item = itemRepository.createItem(userId, item);
        return ItemMapper.MAPPER.itemToItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(userId, itemId);
        return ItemMapper.MAPPER.itemToItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        getUserFromRepository(userId); //user existence check
        return itemRepository.getAllItems(userId).stream()
                .map(ItemMapper.MAPPER::itemToItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, ItemUpdateDto itemUpdateDto, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(userId, itemId);
        if (item.getOwner().getId() != userId)
            throw new AccessForbiddenException("Item update could be performed only by item's owner");
        ItemMapper.MAPPER.itemUpdateToItem(itemUpdateDto, item);
        boolean isUpdated = itemRepository.updateItem(userId, item, itemId);
        if (!isUpdated) throw new InternalServerException(String.format("Could not update item: %s", item));
        return ItemMapper.MAPPER.itemToItemDto(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        getUserFromRepository(userId); //user existence check
        Item item = getItemFromRepository(userId, itemId);
        if (item.getOwner().getId() != userId)
            throw new AccessForbiddenException("Item delete could be performed only by item's owner");
        boolean isDeleted = itemRepository.updateItem(userId, item, itemId);
        if (!isDeleted) throw new InternalServerException(String.format("Could not delete item: %s", item));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return null;
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private Item getItemFromRepository(Long userId, Long itemId) {
        return itemRepository.findItemById(userId, itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class));
    }
}
