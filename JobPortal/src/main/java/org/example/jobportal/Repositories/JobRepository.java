package org.example.jobportal.Repositories;

import org.example.jobportal.Entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findByJobId(Integer jobId);
}
