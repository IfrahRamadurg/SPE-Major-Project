package org.example.jobportal.Services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jobportal.Configurations.JwtService;
import org.example.jobportal.Entities.ApplicantDetails;
import org.example.jobportal.Entities.ApplicantJobMapping;
import org.example.jobportal.Entities.Job;
import org.example.jobportal.Repositories.ApplicantJobMappingRepository;
import org.example.jobportal.Repositories.ApplicantRepository;
import org.example.jobportal.Repositories.JobRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConsentService {
    private final RestTemplate restTemplate;
    private final ApplicantJobMappingRepository applicantJobMappingRepository;
    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;
    private final JwtService jwtService;
    private static final Logger logger = LogManager.getLogger(ConsentService.class);

    public boolean userConsent(HttpServletRequest request, Integer jobId, boolean consent) {
        logger.info("Inside userConsent service");
        Integer loginId = jwtService.extractId(request, "loginId");
        logger.info("loginId: " + loginId);
        Optional<ApplicantDetails> applicantDetails = applicantRepository.findByUser_LoginId(loginId);
        if (applicantDetails.isPresent()) {
            ApplicantDetails applicantDetail = applicantDetails.get();
            Integer applicantId = applicantDetail.getApplicantId();
            logger.info("applicantId: " + applicantId);
            String url = "http://localhost:8083/jobPortal/consent/add";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String requestBody = "empId=" + applicantId + "&jobId=" + jobId + "&consent=" + consent;
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(url, entity, String.class);
            logger.info("response: " + response);
            if ("Failed to add consent to blockchain".equals(response)) {
                return false;
            }

            Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
            if (jobOptional.isPresent()) {
                Job job = jobOptional.get();

                ApplicantJobMapping applicantJobMapping = new ApplicantJobMapping();
                applicantJobMapping.setApplicantDetails(applicantDetail);
                applicantJobMapping.setJob(job);
                applicantJobMapping.setHash(response);
                applicantJobMappingRepository.save(applicantJobMapping);
                logger.info("Added consent to blockchain");
                logger.info("End of userConsent service");
                return true;
            }
        }
        logger.error("Failed to add consent to blockchain");
        logger.info("End of userConsent service");
        return false;
    }

    public List<Integer> getUserConsent(HttpServletRequest request, Integer jobId) {
        logger.info("Inside getUserConsent service");
        List<Integer> users=new ArrayList<>();
       List<ApplicantJobMapping> applicantJobMappingOptional=applicantJobMappingRepository.findByJob_JobId(jobId);
       logger.info("applicantJobMappingOptional: " + applicantJobMappingOptional.toString());
       for(ApplicantJobMapping applicantJobMapping:applicantJobMappingOptional){
           String hash=applicantJobMapping.getHash();
           String url = "http://localhost:8083/jobPortal/consent/get?hash=" + hash;
           logger.info("url: " + url);
           if(Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class))){
               logger.info("applicant id:"+applicantJobMapping.getApplicantDetails().getApplicantId());
               users.add(applicantJobMapping.getApplicantDetails().getApplicantId());
           }
       }
       logger.info("List of users who have given consent: " + users.toString());
       logger.info("End of getUserConsent service");
       return users;
    }
}
