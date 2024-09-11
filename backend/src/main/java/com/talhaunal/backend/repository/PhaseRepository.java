package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    List<Phase> findAllByMentorshipIdOrderByPriorityId(Long mentorshipId);

    List<Phase> findAllByEndDateBetween(Instant startDate, Instant endDate);
}
