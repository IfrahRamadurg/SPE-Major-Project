package org.example.jobportal.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jobportal.Configurations.JwtService;
import org.example.jobportal.JobPortalApplication;
import org.example.jobportal.Models.AuthRequest;
import org.example.jobportal.Models.AuthResponse;
import org.example.jobportal.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        logger.info("Inside register controller");
        AuthResponse response=authService.registerUser(authRequest);
        if(response.getMessage()!=null && !response.getMessage().equals("User registered successfully")) {
            logger.error("Failed to register user");
            logger.error(response.getMessage());
            logger.info("End of Auth register controller");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }else{
            logger.info("successful registration");
            logger.info(response.getMessage());
            logger.info("End of Auth register controller");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        logger.info("Inside authenticate controller");
        AuthResponse response=authService.authenticate(authRequest);
        if(response.getMessage()!=null && !response.getMessage().equals("Successfully logged in")) {
            logger.error("Failed to authenticate user");
            logger.error(response.getMessage());
            logger.info("End of Auth authenticate controller");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }else{
            logger.info("successful authentication");
            logger.info(response.getMessage());
            logger.info("End of Auth authenticate controller");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('Applicant') or hasAuthority('HR')")
    public ResponseEntity<?> logoutUser(HttpServletRequest request){
        logger.info("Inside logout controller");
        jwtService.addToBlacklist(request);
        logger.info("Successfully logged out");
        logger.info("End of Logout controller");
        return ResponseEntity.ok("Successfully logged out");
    }

}
