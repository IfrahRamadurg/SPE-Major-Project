package org.example.jobportal.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Applicant')")
    public String addUserConsent(HttpServletRequest request,@RequestParam Integer jobId, @RequestParam boolean consent) {
        if(consentService.userConsent(request,jobId, consent)) {
            return "Consent added successfully";
        }else{
            return "Failed to add consent";
        }
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('HR')")
    public List<Integer> getUserConsent(HttpServletRequest request, @RequestParam Integer jobId) {
        return consentService.getUserConsent(request, jobId);
    }
}
