package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.request.SubtopicCreateRequest;
import com.talhaunal.backend.controller.response.SubtopicResponse;
import com.talhaunal.backend.domain.Subtopic;
import com.talhaunal.backend.domain.Topic;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.SubtopicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubtopicService {

    private final SubtopicRepository subTopicRepository;
    private final TopicService topicService;


    public SubtopicService(SubtopicRepository subTopicRepository, TopicService topicService) {
        this.subTopicRepository = subTopicRepository;
        this.topicService = topicService;
    }

    public List<SubtopicResponse> get(Long topicId) {
        List<Subtopic> subtopicList = subTopicRepository.findByTopicId(topicId);
        List<SubtopicResponse> subtopicResponseList = new ArrayList<>();
        for (Subtopic subtopic : subtopicList) {
            SubtopicResponse subtopicResponse = new SubtopicResponse();
            subtopicResponse.setId(subtopic.getId());
            subtopicResponse.setName(subtopic.getName());
            subtopicResponseList.add(subtopicResponse);
        }
        return subtopicResponseList;
    }

    public List<Subtopic> findAllById(List<Long> subtopicIds) {
        return subTopicRepository.findAllByIdIn(subtopicIds);
    }

    public void create(SubtopicCreateRequest request) {
        Optional<Subtopic> subtopic = subTopicRepository.findByName(request.getName());
        if (subtopic.isPresent()) {
            throw new BusinessException("subtopic already exists");
        }
        Topic topic = topicService.findByName(request.getTopic())
                .orElseThrow(() -> new DomainNotFoundException("topic not found"));

        Subtopic subtopicEntity = new Subtopic();
        subtopicEntity.setName(request.getName());
        subtopicEntity.setTopic(topic);

        subTopicRepository.save(subtopicEntity);
    }
}
