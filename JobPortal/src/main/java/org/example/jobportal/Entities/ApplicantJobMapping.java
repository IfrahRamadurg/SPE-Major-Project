package org.example.jobportal.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applicantJobMapping")
public class ApplicantJobMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicantId",referencedColumnName = "applicantId")
    private ApplicantDetails applicantDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId",referencedColumnName = "jobId")
    private Job job;

    @Column(name = "hash")
    private String hash;
}
