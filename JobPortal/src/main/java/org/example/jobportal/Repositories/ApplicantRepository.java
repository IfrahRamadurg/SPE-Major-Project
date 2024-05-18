package org.example.jobportal.Repositories;

import org.example.jobportal.Entities.ApplicantDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<ApplicantDetails,Integer> {
    Optional<ApplicantDetails> findByApplicantId(Integer applicantId);
    Optional<ApplicantDetails> findByUser_LoginId(Integer loginId);
}
