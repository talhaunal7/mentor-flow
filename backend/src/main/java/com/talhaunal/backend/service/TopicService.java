package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.request.TopicCreateRequest;
import com.talhaunal.backend.controller.response.TopicResponse;
import com.talhaunal.backend.domain.Topic;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicResponse> get() {
        List<Topic> allTopics = topicRepository.findAll();
        List<TopicResponse> responses = new ArrayList<>();
        for (Topic topic : allTopics) {
            TopicResponse topicResponse = new TopicResponse();
            topicResponse.setName(topic.getName());
            topicResponse.setId(topic.getId());
            responses.add(topicResponse);
        }
        return responses;
    }

    public Topic findById(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new DomainNotFoundException("Topic not found"));
    }

    public void create(TopicCreateRequest request) {
        Optional<Topic> topic = findByName(request.getName());
        if (topic.isPresent()) {
            throw new BusinessException("Topic already exists");
        }
        Topic topicEntity = new Topic();
        topicEntity.setName(request.getName());
        topicRepository.save(topicEntity);
    }

    public Optional<Topic> findByName(String name) {
        return topicRepository.findByName(name);
    }
}
