package com.talhaunal.backend.util;

import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.security.AppUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    private AuthUtils() {
    }

    public static AppUserDetails getAuthenticatedUser() {
        return (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getAuthenticatedUserId() {
        AppUserDetails principal = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getUsername();
    }

    public static String getAuthenticatedUserRole() {
        AppUserDetails principal = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GrantedAuthority authority = principal
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("set user role on ldif"));

        return authority.getAuthority();
    }
}
