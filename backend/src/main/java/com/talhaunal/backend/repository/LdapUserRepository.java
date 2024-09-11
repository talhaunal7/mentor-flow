package com.talhaunal.backend.repository;

import com.talhaunal.backend.domain.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.Optional;

public interface LdapUserRepository extends LdapRepository<LdapUser> {
    Optional<LdapUser> findByUid(String uid);
}