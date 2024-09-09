package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        return null;
    }

    @Override
    public ItemRequestDto updateItemRequest(Long userId, Long itemRequestId, ItemRequestUpdateDto itemRequestUpdateDto) {
        return null;
    }

    //Получить список своих запросов вместе с данными об ответах на них.
    //Запросы должны возвращаться отсортированными от более новых к более старым.
    @Override
    public List<ItemRequestDto> getAllOwnItemRequests(Long userId) {
        return null;
    }

    //получить список запросов, созданных другими пользователями.
    @Override
    public List<ItemRequestDto> getAllOthersItemRequests(Long userId) {
        return null;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long itemRequestId) {
        return null;
    }
}
