package org.example.onlineshop.security.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshop.security.AuthenticationRequest;
import org.example.onlineshop.security.AuthenticationResponse;
import org.example.onlineshop.user.model.Role;
import org.example.onlineshop.user.repository.UserRepository;
import org.example.onlineshop.security.CustomUserDetails;
import org.example.onlineshop.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.example.onlineshop.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest req) {
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.Customer)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        User user = userRepository.findByEmail(req.getEmail()).get();
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}

