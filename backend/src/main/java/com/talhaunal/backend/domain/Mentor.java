package com.talhaunal.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talhaunal.backend.domain.enums.MentorStatus;
import com.talhaunal.backend.domain.model.MentorDocument;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mentor")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToMany
    @JoinTable(
            name = "mentor_subtopics",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "subtopic_id")
    )
    private List<Subtopic> subtopics;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MentorStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Subtopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<Subtopic> subtopics) {
        this.subtopics = subtopics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MentorStatus getStatus() {
        return status;
    }

    public void setStatus(MentorStatus status) {
        this.status = status;
    }

    @JsonIgnore
    @Transient
    public MentorDocument toDocument() {
        MentorDocument mentorDocument = new MentorDocument();
        mentorDocument.setId(this.getId());
        mentorDocument.setUserId(this.getUserId());
        mentorDocument.setTopic(this.getTopic().getName());
        mentorDocument.setSubtopics(this.getSubtopics().stream().map(Subtopic::getName).toList());
        mentorDocument.setDescription(this.getDescription());
        mentorDocument.setStatus(this.getStatus());

        return mentorDocument;
    }

    public static final class Builder {
        private Long id;
        private String userId;
        private Topic topic;
        private List<Subtopic> subtopics;
        private String description;
        private MentorStatus status;

        private Builder() {
        }

        public static Builder aMentor() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder topic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public Builder subtopics(List<Subtopic> subtopics) {
            this.subtopics = subtopics;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(MentorStatus status) {
            this.status = status;
            return this;
        }

        public Mentor build() {
            Mentor mentor = new Mentor();
            mentor.setId(id);
            mentor.setUserId(userId);
            mentor.setTopic(topic);
            mentor.setSubtopics(subtopics);
            mentor.setDescription(description);
            mentor.setStatus(status);
            return mentor;
        }
    }
}
