package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.request.MentorCreateRequest;
import com.talhaunal.backend.controller.request.MentorStatusChangeRequest;
import com.talhaunal.backend.controller.response.MentorResponse;
import com.talhaunal.backend.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping
    public void create(@Valid @RequestBody MentorCreateRequest request) {
        mentorService.create(request);
    }

    @GetMapping
    public List<MentorResponse> getAll() {
        return mentorService.getAll();
    }

    @GetMapping("/search")
    public List<MentorResponse> getMentorsByTopicAndSubtopics(@RequestParam(required = false) Long topicId,
                                                              @RequestParam(required = false) List<Long> subtopicIds) {
        return mentorService.getMentorsByTopicAndSubtopics(topicId, subtopicIds);
    }

    @GetMapping("/query")
    public List<MentorResponse> searchMentors(@RequestParam String query) {
        return mentorService.searchMentorsByDescription(query);
    }

    @GetMapping("/{id}")
    public MentorResponse getById(@PathVariable Long id) {
        return mentorService.getResponse(id);
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMINS')")
    public void changeStatus(@PathVariable Long id, @RequestBody MentorStatusChangeRequest request) {
        mentorService.changeStatus(id, request);
    }

    @GetMapping("/uid/{id}")
    public MentorResponse getByUid(@PathVariable String id) {
        return mentorService.getResponseByUid(id);
    }

    @DeleteMapping("/elastic-clear")
    @PreAuthorize("hasAuthority('ROLE_ADMINS')")
    public void clearElasticDocuments(){
        mentorService.clearElastic();
    }

}
