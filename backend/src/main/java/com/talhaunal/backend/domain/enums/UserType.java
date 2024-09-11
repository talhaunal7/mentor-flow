package com.talhaunal.backend.domain.enums;

public enum UserType {
    LDAP,
    GOOGLE;

    public static UserType fromString(String type) {
        return switch (type) {
            case "LDAP" -> LDAP;
            case "GOOGLE" -> GOOGLE;
            default -> throw new EnumConstantNotPresentException(UserType.class, type);
        };
    }
}
