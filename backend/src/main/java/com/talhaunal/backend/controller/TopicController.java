package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.request.TopicCreateRequest;
import com.talhaunal.backend.controller.response.TopicResponse;
import com.talhaunal.backend.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<TopicResponse> get() {
        return topicService.get();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMINS')")
    public void create(@Valid @RequestBody TopicCreateRequest request) {
        topicService.create(request);
    }

}
