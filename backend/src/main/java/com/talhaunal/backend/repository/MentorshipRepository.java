package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.Mentorship;
import com.talhaunal.backend.domain.enums.MentorshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorshipRepository extends JpaRepository<Mentorship, Long> {

    List<Mentorship> findAllByMentor_UserId(String userId);

    List<Mentorship> findAllByMenteeId(String userId);

    Optional<Mentorship> findByMenteeIdAndMentor_Topic_IdAndStatusIsNot(String menteeId, Long topicId, MentorshipStatus status);

    Integer countByMentor_UserIdAndStatusIsNot(String userId, MentorshipStatus status);

}
