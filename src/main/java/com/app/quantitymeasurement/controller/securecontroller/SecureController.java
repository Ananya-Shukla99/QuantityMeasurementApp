package com.app.quantitymeasurement.controller.securecontroller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secure")
public class SecureController {

    // Any logged-in user
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile() {
        return "User profile accessed";
    }

    // USER or ADMIN
    @PostMapping("/compare")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String compare() {
        return "Comparison done";
    }

    // ADMIN only
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        return "Deleted by ADMIN";
    }
}