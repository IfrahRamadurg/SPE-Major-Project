package org.example.jobportal.Services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jobportal.Configurations.JwtService;
import org.example.jobportal.Controllers.AuthController;
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
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public AuthResponse registerUser(AuthRequest request) {
        try{
            logger.info("Inside AuthService registerUser service");
            Optional<User> user1 = repository.findByEmail(request.getEmail());
            if(user1.isPresent()){
                logger.warn("Email already in use");
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
            logger.info("User registered successfully");
            logger.info("End of AuthService registerUser service");
            return AuthResponse.builder()
                    .message("User registered successfully")
                    .build();
        }catch (Exception e){
            logger.error(e.getMessage());
            AuthResponse response=new AuthResponse();
            response.setMessage( e.getMessage());
            logger.info("End of AuthService registerUser service");
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
        logger.info("Inside AuthService authenticate service");
        User user=repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Invalid email or password");
            logger.info("End of AuthService authenticate service");
            return AuthResponse.builder()
                    .message("Invalid email or password")
                    .build();
        }

        if(!user.getRole().equals(request.getRole())) {
            logger.warn("Do not have required access permissions");
            logger.info("End of AuthService authenticate service");
            return AuthResponse.builder()
                    .message("Do not have the required access permissions")
                    .build();
        }

        var jwtToken=jwtService.generateToken(user);
        logger.info("User has logged in successfully");
        logger.info("End of AuthService authenticate service");
        return AuthResponse.builder()
                .token(jwtToken)
                .message("Successfully logged in")
                .build();
    }

}
