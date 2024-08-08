package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemCreateDto) {
        return null;
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return null;
    }

    @Override
    public ItemDto updateItem(Long userId, ItemUpdateDto itemUpdateDto, Long itemId) {
        return null;
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {

    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return null;
    }
}
