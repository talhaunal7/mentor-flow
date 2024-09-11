package com.talhaunal.backend.controller.response;

import java.time.Instant;

public class MentorshipResponse {

    private Long id;
    private Long mentorId;
    private String mentorName;
    private String menteeId;
    private String menteeName;
    private Instant startDate;
    private String topic;
    private String status;

    private boolean mentorFlag;

    public MentorshipResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getMenteeId() {
        return menteeId;
    }

    public void setMenteeId(String menteeId) {
        this.menteeId = menteeId;
    }

    public String getMenteeName() {
        return menteeName;
    }

    public void setMenteeName(String menteeName) {
        this.menteeName = menteeName;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMentorFlag() {
        return mentorFlag;
    }

    public void setMentorFlag(boolean mentorFlag) {
        this.mentorFlag = mentorFlag;
    }


    public static final class Builder {
        private Long id;
        private Long mentorId;
        private String mentorName;
        private String menteeId;
        private String menteeName;
        private Instant startDate;
        private String topic;
        private String status;
        private boolean mentorFlag;

        private Builder() {
        }

        public static Builder aMentorshipResponse() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder mentorId(Long mentorId) {
            this.mentorId = mentorId;
            return this;
        }

        public Builder mentorName(String mentorName) {
            this.mentorName = mentorName;
            return this;
        }

        public Builder menteeId(String menteeId) {
            this.menteeId = menteeId;
            return this;
        }

        public Builder menteeName(String menteeName) {
            this.menteeName = menteeName;
            return this;
        }

        public Builder startDate(Instant startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder mentorFlag(boolean mentorFlag) {
            this.mentorFlag = mentorFlag;
            return this;
        }

        public MentorshipResponse build() {
            MentorshipResponse mentorshipResponse = new MentorshipResponse();
            mentorshipResponse.setId(id);
            mentorshipResponse.setMentorId(mentorId);
            mentorshipResponse.setMentorName(mentorName);
            mentorshipResponse.setMenteeId(menteeId);
            mentorshipResponse.setMenteeName(menteeName);
            mentorshipResponse.setStartDate(startDate);
            mentorshipResponse.setTopic(topic);
            mentorshipResponse.setStatus(status);
            mentorshipResponse.setMentorFlag(mentorFlag);
            return mentorshipResponse;
        }
    }
}
