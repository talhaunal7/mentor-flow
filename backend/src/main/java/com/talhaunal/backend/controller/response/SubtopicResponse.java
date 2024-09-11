package com.talhaunal.backend.controller.response;

public class SubtopicResponse {

    private Long id;
    private String name;


    public SubtopicResponse() {
    }

    public SubtopicResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
