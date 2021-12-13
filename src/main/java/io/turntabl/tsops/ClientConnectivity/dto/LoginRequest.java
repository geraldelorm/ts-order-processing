package io.turntabl.tsops.ClientConnectivity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;
}
