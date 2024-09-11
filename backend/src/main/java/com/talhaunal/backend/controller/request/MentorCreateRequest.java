package com.talhaunal.backend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MentorCreateRequest {

    @NotNull(message = "topic has to be chosen")
    private Long topicId;

    @NotEmpty(message = "you have to pick at least one subtopic")
    private List<Long> subtopicIds;

    @NotBlank(message = "description field must be filled")
    private String description;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public List<Long> getSubtopicIds() {
        return subtopicIds;
    }

    public void setSubtopicIds(List<Long> subtopicIds) {
        this.subtopicIds = subtopicIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
