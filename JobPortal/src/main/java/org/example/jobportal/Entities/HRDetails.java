package org.example.jobportal.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="HRDetails")
public class HRDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hrId")
    private Integer hrId;

    @Column(name="name")
    private String name;

    @Column(name="org")
    private String org;

    @Column(name="phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loginId", referencedColumnName = "loginId")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "hrDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    List<Job> jobs;
}
