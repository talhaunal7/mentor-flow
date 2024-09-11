package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.Mentor;
import com.talhaunal.backend.domain.enums.MentorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    Optional<Mentor> findByUserId(String userId);


    @Query("SELECT m FROM Mentor m JOIN m.subtopics s WHERE m.status = :status AND (:topicId IS NULL OR m.topic.id = :topicId) AND " +
            "(:subtopicIds IS NULL OR s.id IN :subtopicIds)")
    List<Mentor> findByTopicAndSubtopicsAndStatus(Long topicId,
                                                  List<Long> subtopicIds,
                                                  MentorStatus status);


}
