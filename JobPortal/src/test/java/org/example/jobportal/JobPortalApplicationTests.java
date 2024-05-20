package org.example.jobportal;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jobportal.Controllers.ConsentController;
import org.example.jobportal.Entities.Role;
import org.example.jobportal.Models.AuthRequest;
import org.example.jobportal.Services.AuthService;
import org.example.jobportal.Services.ConsentService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class JobPortalApplicationTests {

	private static final Logger logger = LogManager.getLogger(ConsentController.class);

	@Autowired
	private AuthService authService;

	@Test
	void contextLoads() {
	}

	@BeforeAll
	static void testInit() {
		logger.info("Starting testcases");
	}

	@Test
	void testPositiveLoginDetails(){
		AuthRequest authRequest = new AuthRequest();
		authRequest.setEmail("shreya@iiitb.ac.in");
		authRequest.setPassword("shreya");
		authRequest.setRole(Role.valueOf("Applicant"));
		assertEquals("Successfully logged in", authService.authenticate(authRequest).getMessage());
	}

	@Test
	void testNegativeLoginDetails(){
		AuthRequest authRequest = new AuthRequest();
		authRequest.setEmail("rutuja@iiitb.ac.in");
		authRequest.setPassword("rutu");
		authRequest.setRole(Role.valueOf("HR"));
		assertNotEquals("Successfully logged in",authService.authenticate(authRequest).getMessage());
	}

	@AfterAll
	static void testComplete() {
		logger.info("All testcases completed");
	}
}
