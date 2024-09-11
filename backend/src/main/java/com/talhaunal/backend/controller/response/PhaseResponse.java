package com.talhaunal.backend.controller.response;

import com.talhaunal.backend.domain.enums.PhaseStatus;

import java.time.Instant;

public class PhaseResponse {

    private Long id;

    private String name;

    private Integer priorityId;

    private Instant endDate;

    private PhaseStatus status;

    private Integer mentorRating;

    private String mentorReview;

    private Integer menteeRating;

    private String menteeReview;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public PhaseStatus getStatus() {
        return status;
    }

    public void setStatus(PhaseStatus status) {
        this.status = status;
    }

    public Integer getMentorRating() {
        return mentorRating;
    }

    public void setMentorRating(Integer mentorRating) {
        this.mentorRating = mentorRating;
    }

    public String getMentorReview() {
        return mentorReview;
    }

    public void setMentorReview(String mentorReview) {
        this.mentorReview = mentorReview;
    }

    public Integer getMenteeRating() {
        return menteeRating;
    }

    public void setMenteeRating(Integer menteeRating) {
        this.menteeRating = menteeRating;
    }

    public String getMenteeReview() {
        return menteeReview;
    }

    public void setMenteeReview(String menteeReview) {
        this.menteeReview = menteeReview;
    }

}
