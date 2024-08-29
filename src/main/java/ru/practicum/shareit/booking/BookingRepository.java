package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b
            from Booking as b
            join fetch b.booker as br
            join fetch b.item as i
            join fetch i.owner as o
            where b.id = ?1 and br.id = ?2
            or b.id = ?1 and o.id = ?2
            """)
    Optional<Booking> findByIdWhereBookerIdOrItemOwnerId(Long bookingId, Long userId);

    List<Booking> findAllByBookerId(Long bookerId);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndEndLessThan(Long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartGreaterThan(Long bookerId, LocalDateTime dateTime);

    @Query("""
            select b
            from Booking as b
            join fetch b.booker as br
            where br.id = ?1 and b.start <= ?2 and b.end >= ?2
            """)
    List<Booking> findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerId(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndEndLessThan(Long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartGreaterThan(Long ownerId, LocalDateTime dateTime);

    @Query("""
            select b
            from Booking as b
            join fetch b.booker as br
            join fetch b.item as i
            join fetch i.owner as o
            where o.id = ?1 and b.start <= ?2 and b.end >= ?2
            """)
    List<Booking> findAllByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long ownerId, LocalDateTime dateTime);

    @Query("""
            select b, min(b.start)
            from Booking as b
            join fetch b.item as i
            join i.owner as o
            where o.id = ?1 and b.start > ?2
            group by b, i.id
            """)
    List<Booking> findAllByItemOwnerIdNextItemBookingFromDate(Long ownerId, LocalDateTime dateTime);

    @Query("""
            select b, max(b.start)
            from Booking as b
            join fetch b.item as i
            join i.owner as o
            where o.id = ?1 and b.start < ?2
            group by b, i.id
            """)
    List<Booking> findAllByItemOwnerIdLastItemBookingFromDate(Long ownerId, LocalDateTime dateTime);
}