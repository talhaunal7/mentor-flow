package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.response.UserResponse;
import com.talhaunal.backend.domain.GoogleUser;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.GoogleUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleUserService {

    private final GoogleUserRepository googleUserRepository;

    public GoogleUserService(GoogleUserRepository googleUserRepository) {
        this.googleUserRepository = googleUserRepository;
    }

    public GoogleUser save(GoogleUser googleUser) {
        return googleUserRepository.save(googleUser);
    }

    public Optional<GoogleUser> findById(String id) {
        return googleUserRepository.findById(id);
    }

    public GoogleUser getById(String id) {
        return googleUserRepository.findById(id).orElseThrow(()-> new DomainNotFoundException("Google user not found"));
    }

    public UserResponse getResponseById(String id) {
        GoogleUser googleUser =  getById(id);
        UserResponse userResponse = new UserResponse();

        userResponse.setUid(googleUser.getEmail());
        userResponse.setName(googleUser.getName());
        userResponse.setSurname(googleUser.getSurname());
        userResponse.setRole(googleUser.getRoles());
        return userResponse;
    }
}
