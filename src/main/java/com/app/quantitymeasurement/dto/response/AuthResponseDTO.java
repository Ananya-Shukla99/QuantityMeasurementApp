package com.app.quantitymeasurement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

 private String token;
 private String email;
 private String role;
 private String name;
 private String tokenType = "Bearer";
 private long expiresIn;
 

 public AuthResponseDTO(String token, String email, String role, String name) {
     this.token = token;
     this.email = email;
     this.role = role;
     this.tokenType = "Bearer";
     this.expiresIn = 86400000L;
     this.name= name;
 }
}