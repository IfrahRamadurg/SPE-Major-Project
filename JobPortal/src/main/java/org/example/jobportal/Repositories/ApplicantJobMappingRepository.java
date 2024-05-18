package org.example.jobportal.Repositories;

import org.example.jobportal.Entities.ApplicantJobMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantJobMappingRepository extends JpaRepository<ApplicantJobMapping, Integer> {
    Optional<ApplicantJobMapping> findByApplicantDetails_ApplicantIdAndJob_JobId(Integer applicantId, Integer jobId);
    List<ApplicantJobMapping> findByJob_JobId(Integer jobId);
}
