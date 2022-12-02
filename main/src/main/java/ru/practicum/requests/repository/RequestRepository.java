package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE requests SET status = 'REJECTED' WHERE event_id = ?1", nativeQuery = true)
    void rejectAll(Long eventId);

    List<Request> findByEvent_IdAndStatus(Long id, Status status);

    int countByEvent_IdAndStatus(Long eventId, Status status);
}
