package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestUpdateDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestStatus;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        User requester = getUserFromRepository(userId);
        ItemRequest itemRequest = itemRequestMapper.itemRequestCreateToItemRequest(itemRequestCreateDto);
        itemRequest.setRequester(requester);
        itemRequest.setStatus(ItemRequestStatus.ACTIVE);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
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

    private User getUserFromRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }
}
