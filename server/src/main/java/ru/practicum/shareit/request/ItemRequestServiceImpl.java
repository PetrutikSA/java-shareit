package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestStatus;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    public List<ItemRequestDto> getAllOwnItemRequests(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId);
        return itemRequests.stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequestMapper::itemRequestToItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllOthersItemRequests(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNot(userId);
        return itemRequests.stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequestMapper::itemRequestToItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long itemRequestId) {
        ItemRequest itemRequest = getItemRequestFromRepository(itemRequestId);
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
    }

    private User getUserFromRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private ItemRequest getItemRequestFromRepository(Long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, ItemRequest.class));
    }
}
