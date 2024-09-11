package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.request.SubtopicCreateRequest;
import com.talhaunal.backend.controller.response.SubtopicResponse;
import com.talhaunal.backend.service.SubtopicService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtopics")
public class SubtopicController {
    private final SubtopicService subtopicService;

    public SubtopicController(SubtopicService subtopicService) {
        this.subtopicService = subtopicService;
    }

    @GetMapping("/{topicId}")
    public List<SubtopicResponse> get(@PathVariable Long topicId){
        return subtopicService.get(topicId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMINS')")
    public void create(@Valid @RequestBody SubtopicCreateRequest request){
        subtopicService.create(request);
    }

}
