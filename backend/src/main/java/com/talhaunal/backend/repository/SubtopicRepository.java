package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtopicRepository extends JpaRepository<Subtopic, Long> {
    List<Subtopic> findByTopicId(Long topicId);

    List<Subtopic> findAllByIdIn(List<Long> ids);

    Optional<Subtopic> findByName(String name);
}
