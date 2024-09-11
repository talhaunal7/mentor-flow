package com.talhaunal.backend.controller.response;

import com.talhaunal.backend.domain.enums.MentorStatus;

import java.util.List;

public class MentorResponse {

    private Long id;
    private String userId;
    private String topic;
    private List<String> subtopics;
    private String name;
    private String surname;
    private String description;
    private MentorStatus status;
    private String mail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static final class MentorResponseBuilder {
        private Long id;
        private String userId;
        private String topic;
        private List<String> subtopics;
        private String name;
        private String surname;
        private String description;
        private MentorStatus status;
        private String mail;

        private MentorResponseBuilder() {
        }

        public static MentorResponseBuilder aMentorResponse() {
            return new MentorResponseBuilder();
        }

        public MentorResponseBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public MentorResponseBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public MentorResponseBuilder withTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public MentorResponseBuilder withSubtopics(List<String> subtopics) {
            this.subtopics = subtopics;
            return this;
        }

        public MentorResponseBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MentorResponseBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public MentorResponseBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MentorResponseBuilder withStatus(MentorStatus status) {
            this.status = status;
            return this;
        }

        public MentorResponseBuilder withMail(String mail) {
            this.mail = mail;
            return this;
        }

        public MentorResponse build() {
            MentorResponse mentorResponse = new MentorResponse();
            mentorResponse.setId(id);
            mentorResponse.setUserId(userId);
            mentorResponse.setTopic(topic);
            mentorResponse.setSubtopics(subtopics);
            mentorResponse.setName(name);
            mentorResponse.setSurname(surname);
            mentorResponse.setDescription(description);
            mentorResponse.setStatus(status);
            mentorResponse.setMail(mail);
            return mentorResponse;
        }
    }
}
