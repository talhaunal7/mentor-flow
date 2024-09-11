package com.talhaunal.backend.service;

import com.talhaunal.backend.controller.response.UserResponse;
import com.talhaunal.backend.domain.LdapUser;
import com.talhaunal.backend.exception.DomainNotFoundException;
import com.talhaunal.backend.repository.LdapUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LdapUserService {

    private final LdapUserRepository ldapUserRepository;

    public LdapUserService(LdapUserRepository ldapUserRepository) {
        this.ldapUserRepository = ldapUserRepository;
    }

    public UserResponse getUserByUid(String uid) {
        LdapUser ldapUser = getByUid(uid);
        UserResponse userResponse = new UserResponse();

        userResponse.setUid(ldapUser.getUid());
        userResponse.setName(ldapUser.getCommonName());
        userResponse.setSurname(ldapUser.getSurname());

        return userResponse;
    }

    public LdapUser getByUid(String id) {
        return ldapUserRepository.findByUid(id).orElseThrow(() -> new DomainNotFoundException("User not found"));
    }

    public Optional<LdapUser> findByUid(String id) {
        return ldapUserRepository.findByUid(id);
    }


}
