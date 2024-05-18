package org.example.jobportal.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobId")
    private Integer jobId;

    @Column(name = "Role")
    private String role;

    @Column(name="Description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hrId",referencedColumnName = "hrId")
    private HRDetails hrDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "job",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ApplicantJobMapping> applicantJobMappings;
}
