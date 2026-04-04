package com.app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequestDTO - Request object for user login
 *
 * Contains credentials needed for authentication:
 * - Email (valid email format)
 * - Password (non-empty)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    /** User's email address */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /** User's password */
    @NotBlank(message = "Password is required")
    private String password;
}

