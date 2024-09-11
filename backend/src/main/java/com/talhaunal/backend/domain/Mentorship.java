package com.talhaunal.backend.domain;

import com.talhaunal.backend.domain.enums.MentorshipStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "mentorship")
public class Mentorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(name = "mentee_id", nullable = false)
    private String menteeId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MentorshipStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public String getMenteeId() {
        return menteeId;
    }

    public void setMenteeId(String menteeId) {
        this.menteeId = menteeId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public MentorshipStatus getStatus() {
        return status;
    }

    public void setStatus(MentorshipStatus status) {
        this.status = status;
    }
}

