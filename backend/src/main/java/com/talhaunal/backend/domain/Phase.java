package com.talhaunal.backend.domain;

import com.talhaunal.backend.domain.enums.PhaseStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "phase")
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorship_id", nullable = false)
    private Mentorship mentorship;

    @Column(name = "name")
    private String name;

    @Column(name = "priority_id")
    private Integer priorityId;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PhaseStatus status;

    @Column(name = "mentor_rating")
    private Integer mentorRating;

    @Column(name = "mentor_review")
    private String mentorReview;

    @Column(name = "mentee_rating")
    private Integer menteeRating;

    @Column(name = "mentee_review")
    private String menteeReview;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentorship getMentorship() {
        return mentorship;
    }

    public void setMentorship(Mentorship mentorship) {
        this.mentorship = mentorship;
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
