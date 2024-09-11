package com.talhaunal.backend.domain.model;

import com.talhaunal.backend.domain.enums.MentorStatus;
import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Objects;

@Document(indexName = "mentors")
public class MentorDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String userId;

    @Field(type = FieldType.Text)
    private String topic;

    @Field(type = FieldType.Text)
    private List<String> subtopics;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<String> subtopics) {
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

    @Override
    public String toString() {
        return "Mentor{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", topic='" + topic + '\'' +
                ", subtopics=" + subtopics +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MentorDocument mentor = (MentorDocument) o;
        return Objects.equals(id, mentor.id) && Objects.equals(userId, mentor.userId) && Objects.equals(topic, mentor.topic) && Objects.equals(subtopics, mentor.subtopics) && Objects.equals(description, mentor.description) && status == mentor.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, topic, subtopics, description, status);
    }

}
