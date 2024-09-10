package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    public List<ItemRequest> findAllByRequesterId(Long requesterId);

    public List<ItemRequest> findAllByRequesterIdNot(Long requesterId);
}
