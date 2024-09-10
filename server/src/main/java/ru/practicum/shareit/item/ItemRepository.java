package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long userId);

    @Query("""
            select i
            from Item as i
            where upper(i.name) like upper(concat('%', ?1, '%')) and i.available = true
            or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true
            """)
    List<Item> findAllByNameOrDescription(String text);
}