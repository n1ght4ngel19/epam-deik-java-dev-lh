package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.models.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    Optional<Screening> findByMovieTitleAndRoomNameAndStartDateTime(
            String movieTitle, String roomName, String startDateTime);
}
