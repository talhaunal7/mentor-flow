package com.talhaunal.backend.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PhaseCreateRequest {
    @Valid
    @NotNull
    List<PhaseDetailRequest> phaseDetails;

    @NotNull
    private Long mentorshipId;

    public List<PhaseDetailRequest> getPhaseDetails() {
        return phaseDetails;
    }

    public void setPhaseDetails(List<PhaseDetailRequest> phaseDetails) {
        this.phaseDetails = phaseDetails;
    }

    public Long getMentorshipId() {
        return mentorshipId;
    }

    public void setMentorshipId(Long mentorshipId) {
        this.mentorshipId = mentorshipId;
    }
}
