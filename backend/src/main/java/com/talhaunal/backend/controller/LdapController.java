package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.response.UserResponse;
import com.talhaunal.backend.service.LdapUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LdapController {

    private final LdapUserService ldapUserService;

    public LdapController(LdapUserService ldapUserService) {
        this.ldapUserService = ldapUserService;
    }

    @GetMapping("/{uid}")
    public UserResponse getUserByUid(@PathVariable String uid) {
        return ldapUserService.getUserByUid(uid);
    }

}
