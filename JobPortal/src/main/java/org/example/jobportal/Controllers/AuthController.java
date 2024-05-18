package org.example.jobportal.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobportal.Models.AuthRequest;
import org.example.jobportal.Models.AuthResponse;
import org.example.jobportal.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        AuthResponse response=authService.registerUser(authRequest);
        if(response.getMessage()!=null && !response.getMessage().equals("User registered successfully")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }else{
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        AuthResponse response=authService.authenticate(authRequest);
        if(response.getMessage()!=null && !response.getMessage().equals("Successfully logged in")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }else{
            return ResponseEntity.ok(response);
        }
    }
}
