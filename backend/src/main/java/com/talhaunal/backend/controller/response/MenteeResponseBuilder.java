package com.talhaunal.backend.controller.response;

public final class MenteeResponseBuilder {
    private String fullName;
    private String mail;

    private MenteeResponseBuilder() {
    }

    public static MenteeResponseBuilder aMenteeResponse() {
        return new MenteeResponseBuilder();
    }

    public MenteeResponseBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public MenteeResponseBuilder withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public MenteeResponse build() {
        MenteeResponse menteeResponse = new MenteeResponse();
        menteeResponse.setFullName(fullName);
        menteeResponse.setMail(mail);
        return menteeResponse;
    }
}
