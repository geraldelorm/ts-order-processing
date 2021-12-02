package io.turntabl.tsops.ClientConnectivity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userRole;
    private String authenticationToken;
    private String refreshToken;
    private LocalDateTime expiresAt;
}
