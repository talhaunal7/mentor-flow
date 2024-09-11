package com.talhaunal.backend.controller.request;

public class MentorshipCreateRequest {
    private Long mentorId;

    public MentorshipCreateRequest() {
    }

    public MentorshipCreateRequest(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }
}
