package com.talhaunal.backend.controller.request;

import java.time.Instant;

public class PhaseAddRequest {

    private Instant endDate;
    private String name;
    private Integer priorityId;

    public PhaseAddRequest() {
    }

    public PhaseAddRequest(Instant endDate, String name, Integer priorityId) {
        this.endDate = endDate;
        this.name = name;
        this.priorityId = priorityId;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
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
}
