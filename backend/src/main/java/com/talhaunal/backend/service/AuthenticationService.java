package com.talhaunal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.talhaunal.backend.controller.request.LoginRequest;
import com.talhaunal.backend.controller.request.OauthLoginRequest;
import com.talhaunal.backend.controller.response.UserResponse;
import com.talhaunal.backend.domain.GoogleUser;
import com.talhaunal.backend.domain.enums.UserType;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.security.AppUserDetails;
import com.talhaunal.backend.security.JwtTokenProvider;
import com.talhaunal.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final GoogleUserService googleUserService;
    private final LdapUserService ldapUserService;
    private final GoogleIdTokenVerifier verifier;

    public AuthenticationService(@Value("${app.googleClientId}") String clientId,
                                 GoogleUserService googleUserService,
                                 AuthenticationManagerBuilder authenticationManagerBuilder,
                                 JwtTokenProvider tokenProvider,
                                 LdapUserService ldapUserService) {
        this.googleUserService = googleUserService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.ldapUserService = ldapUserService;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(clientId)).build();
    }

    public String authorize(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        SecurityContextHolder.getContext().setAuthentication(null);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    public String oauthAuthorize(OauthLoginRequest oauthLoginRequest) {
        GoogleUser googleUser = verifyIDToken(oauthLoginRequest.getIdToken());
        googleUser = createOrUpdateUser(googleUser);
        return tokenProvider.generateToken(googleUser);
    }

    public GoogleUser createOrUpdateUser(GoogleUser googleUser) {
        Optional<GoogleUser> existingAccount = googleUserService.findById(googleUser.getEmail());
        if (existingAccount.isEmpty()) {
            googleUserService.save(googleUser);
            return googleUser;
        }
        googleUser.setName(googleUser.getName());
        googleUser.setSurname(googleUser.getSurname());
        googleUserService.save(googleUser);
        return googleUser;
    }

    private GoogleUser verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();

            return new GoogleUser(email, firstName, lastName, "ROLE_USERS");
        } catch (GeneralSecurityException | IOException e) {
            throw new BusinessException("Google authentication error");
        }
    }

    public UserResponse verifyToken() {
        AppUserDetails authenticatedUser = AuthUtils.getAuthenticatedUser();
        String authority = authenticatedUser
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("user has no authority, set ldif file")).getAuthority();

        UserResponse userResponse = null;
        if (UserType.GOOGLE == UserType.fromString(authenticatedUser.getType())) {
            userResponse = googleUserService.getResponseById(authenticatedUser.getUsername());
        } else if (UserType.LDAP == UserType.fromString(authenticatedUser.getType())) {
            userResponse = ldapUserService.getUserByUid(authenticatedUser.getUsername());
            userResponse.setRole(authority);
        }
        return userResponse;
    }
}
