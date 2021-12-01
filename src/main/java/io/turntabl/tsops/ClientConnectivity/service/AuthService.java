package io.turntabl.tsops.ClientConnectivity.service;


import io.turntabl.tsops.ClientConnectivity.dto.AuthenticationResponse;
import io.turntabl.tsops.ClientConnectivity.dto.LoginRequest;
import io.turntabl.tsops.ClientConnectivity.dto.RefreshTokenRequest;
import io.turntabl.tsops.ClientConnectivity.dto.RegisterRequest;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.ClientConnectivity.security.jwtUtils.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {

            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreatedAt(new Date());
            user.setUserRole(registerRequest.getUserRole());
            userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername());
    }


    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .userid(getCurrentUser().getId())
                .firstName(getCurrentUser().getFirstName())
                .lastName(getCurrentUser().getLastName())
                .email(getCurrentUser().getEmail())
                .userRole(getCurrentUser().getUserRole())
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProvider.getJwtExpirationInMillis()))
                .build();

    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());

        return AuthenticationResponse.builder()
                .email(refreshTokenRequest.getEmail())
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProvider.getJwtExpirationInMillis()))
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }


}