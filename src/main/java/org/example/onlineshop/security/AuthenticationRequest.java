package org.example.onlineshop.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
