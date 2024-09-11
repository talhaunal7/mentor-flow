package com.talhaunal.backend.controller.request;

import com.talhaunal.backend.domain.enums.MentorStatus;

public class MentorStatusChangeRequest {

    MentorStatus status;

    public MentorStatusChangeRequest() {
    }

    public MentorStatusChangeRequest(MentorStatus status) {
        this.status = status;
    }

    public MentorStatus getStatus() {
        return status;
    }

    public void setStatus(MentorStatus status) {
        this.status = status;
    }
}
