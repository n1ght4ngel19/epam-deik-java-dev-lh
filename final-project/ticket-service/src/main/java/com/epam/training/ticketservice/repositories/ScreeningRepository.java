package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.models.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    Optional<Screening> findByTitleAndRoomAndStartTime(
            String movieTitle, String roomName, LocalDateTime startDateTime);

    boolean existsByRoomAndStartTimeBetween(
            String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean existsByRoomAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(
            String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<Screening> findByRoomAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(
            String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Optional<Screening>> findByRoom(String roomName);
}
