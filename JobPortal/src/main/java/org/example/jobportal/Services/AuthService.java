package org.example.jobportal.Services;

import lombok.RequiredArgsConstructor;
import org.example.jobportal.Configurations.JwtService;
import org.example.jobportal.Entities.User;
import org.example.jobportal.Models.AuthRequest;
import org.example.jobportal.Models.AuthResponse;
import org.example.jobportal.Repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthResponse registerUser(AuthRequest request) {
        try{
            Optional<User> user1 = repository.findByEmail(request.getEmail());
            if(user1.isPresent()){
                return AuthResponse.builder()
                        .message("Email already exists")
                        .build();
            }
            var user= User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();
            userRepository.save(user);
            return AuthResponse.builder()
                    .message("User registered successfully")
                    .build();
        }catch (Exception e){
            AuthResponse response=new AuthResponse();
            response.setMessage( e.getMessage());
            return response;
        }

    }

    public AuthResponse authenticate(AuthRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        User user=repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .message("Invalid username or password")
                    .build();
        }

        var jwtToken=jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .message("Successfully logged in")
                .build();
    }

}
