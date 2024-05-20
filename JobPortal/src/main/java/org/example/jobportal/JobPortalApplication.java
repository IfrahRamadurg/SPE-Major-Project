package org.example.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import  org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class JobPortalApplication {

	private static final Logger logger = LogManager.getLogger(JobPortalApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JobPortalApplication.class, args);
	}

}
