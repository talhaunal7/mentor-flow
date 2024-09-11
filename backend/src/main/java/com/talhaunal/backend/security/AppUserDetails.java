package com.talhaunal.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class AppUserDetails extends User {

    private static final long serialVersionUID = 3000038732509478732L;

    private String serialNumber;
    private List<String> profiles;
    private String type;

    public AppUserDetails(String username, String serialNumber, List<String> profiles, Collection<? extends GrantedAuthority> authorities, String type) {
        super(username, "1", authorities);
        this.serialNumber = serialNumber;
        this.profiles = profiles;
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
