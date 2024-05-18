package org.example.jobportal.Models;

import lombok.*;
import org.example.jobportal.Entities.Role;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
    private Role role;
}
