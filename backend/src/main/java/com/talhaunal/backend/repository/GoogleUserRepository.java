package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
}