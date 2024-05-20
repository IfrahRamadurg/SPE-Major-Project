package org.example.jobportal.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jobportal.Services.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobPortal/consent")
public class ConsentController {
    @Autowired
    private final ConsentService consentService;
    private static final Logger logger = LogManager.getLogger(ConsentController.class);

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Applicant')")
    public String addUserConsent(HttpServletRequest request,@RequestParam Integer jobId, @RequestParam boolean consent) {
        logger.info("Inside addUserConsent Controller");
        if(consentService.userConsent(request,jobId, consent)) {
            logger.info("User consent added");
            logger.info("End of addUserConsent Controller");
            return "Consent added successfully";
        }else{
            logger.error("User consent not added");
            logger.error("End of addUserConsent Controller");
            return "Failed to add consent";
        }
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('HR')")
    public List<Integer> getUserConsent(HttpServletRequest request, @RequestParam Integer jobId) {
        logger.info("Inside getUserConsent Controller");
        logger.info("End of getUserConsent Controller");
        return consentService.getUserConsent(request, jobId);
    }
}
