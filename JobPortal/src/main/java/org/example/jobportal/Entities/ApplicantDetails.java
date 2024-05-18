package org.example.jobportal.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
@Table(name="ApplicantDetails")
public class ApplicantDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="applicantId")
    private Integer applicantId;

    @Column(name="name")
    private String name;

    @Column(name="age")
    private Integer age;

    @Column(name="experience")
    private Integer experience;

    @Column(name = "resume")
    private String resume;// make the changes as required

    @Column(name="address")
    private String address;

    @Column(name="description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loginId", referencedColumnName = "loginId")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "applicantDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    List<ApplicantJobMapping> applicantJobMappings;
}
