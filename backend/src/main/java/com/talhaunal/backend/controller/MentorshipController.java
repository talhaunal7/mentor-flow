package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.response.MentorshipResponse;
import com.talhaunal.backend.service.MentorshipService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorships")
public class MentorshipController {

    private final MentorshipService mentorshipService;

    public MentorshipController(MentorshipService mentorshipService) {
        this.mentorshipService = mentorshipService;
    }

    @GetMapping("/{id}")
    public MentorshipResponse get(@PathVariable Long id) {
        return mentorshipService.getResponse(id);
    }

    @GetMapping("/mentor-processes")
    public List<MentorshipResponse> getMentorProcesses() {
        return mentorshipService.getMentorProcesses();
    }

    @GetMapping("/mentee-processes")
    public List<MentorshipResponse> getMenteeProcesses() {
        return mentorshipService.getMenteeProcesses();
    }

    @PostMapping("/pick-mentor/{mentorId}")
    public void pickMentor(@NotNull @PathVariable Long mentorId) {
        mentorshipService.pickMentor(mentorId);
    }

}
