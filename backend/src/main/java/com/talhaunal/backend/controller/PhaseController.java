package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.request.PhaseCompletionRequest;
import com.talhaunal.backend.controller.request.PhaseCreateRequest;
import com.talhaunal.backend.controller.response.PhaseResponse;
import com.talhaunal.backend.service.PhaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping("/{mentorshipId}")
    public List<PhaseResponse> get(@PathVariable Long mentorshipId) {
        return phaseService.get(mentorshipId);
    }

    @PostMapping
    public void create(@Valid @RequestBody PhaseCreateRequest request) {
        phaseService.create(request);
    }

    @PutMapping("/{id}/completion")
    public void completion(@PathVariable Long id,@Valid @RequestBody PhaseCompletionRequest request) {
        phaseService.completion(id, request);
    }
}
