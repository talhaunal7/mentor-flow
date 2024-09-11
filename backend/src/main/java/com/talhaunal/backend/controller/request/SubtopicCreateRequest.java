package com.talhaunal.backend.controller.request;

import jakarta.validation.constraints.NotBlank;

public class SubtopicCreateRequest {

    @NotBlank(message="couldnt fetch topic")
    private String topic;

    @NotBlank(message = "subtopic can't be empty")
    private String name;

    public SubtopicCreateRequest() {
    }

    public SubtopicCreateRequest(String topic, String name) {
        this.topic = topic;
        this.name = name;
    }

    public @NotBlank String getTopic() {
        return topic;
    }

    public void setTopic(@NotBlank String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
