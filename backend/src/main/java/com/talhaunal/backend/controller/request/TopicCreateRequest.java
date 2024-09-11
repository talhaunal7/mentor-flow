package com.talhaunal.backend.controller.request;


import jakarta.validation.constraints.NotBlank;

public class TopicCreateRequest {

    @NotBlank(message = "topic name can't be emty")
    private String name;

    public TopicCreateRequest() {
    }

    public TopicCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
