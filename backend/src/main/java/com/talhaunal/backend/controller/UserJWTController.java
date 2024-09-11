package com.talhaunal.backend.controller;

import com.talhaunal.backend.controller.request.LoginRequest;
import com.talhaunal.backend.controller.request.OauthLoginRequest;
import com.talhaunal.backend.controller.response.LoginResponse;
import com.talhaunal.backend.controller.response.UserResponse;
import com.talhaunal.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final AuthenticationService authenticationService;

    public UserJWTController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authorize(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.authorize(loginRequest);
        HttpHeaders httpHeaders = getHttpHeaders(token);
        return new ResponseEntity<>(new LoginResponse(token), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<LoginResponse> oauthAuthorize(@Valid @RequestBody OauthLoginRequest oauthLoginRequest) {
        String token = authenticationService.oauthAuthorize(oauthLoginRequest);
        HttpHeaders httpHeaders = getHttpHeaders(token);
        return new ResponseEntity<>(new LoginResponse(token), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<UserResponse> authorize() {
        UserResponse userResponse = authenticationService.verifyToken();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
    }


    private HttpHeaders getHttpHeaders(String token) {
        ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return httpHeaders;
    }


}
