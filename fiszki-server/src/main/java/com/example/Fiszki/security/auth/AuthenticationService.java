package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.user.ChangePasswordRequest;
import com.example.Fiszki.security.auth.user.PointsRequest;
import com.example.Fiszki.security.auth.user.UserInfoResponse;
import com.example.Fiszki.security.auth.user.UserLVLResponse;
import com.example.Fiszki.security.config.JwtService;
import com.example.Fiszki.security.user.Role;
import com.example.Fiszki.security.user.User;
import com.example.Fiszki.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private TokenInstance tokenInstance = TokenInstance.getInstance();
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Sprawdzenie pustych pól
        if (request.getFirstname().isEmpty() || request.getLastname().isEmpty() ||
                request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            return AuthenticationResponse.builder().response("All fields must be filled in.").build();
        }

        // Sprawdzenie poprawności adresu e-mail
        if (!isValidEmail(request.getEmail())) {
            return AuthenticationResponse.builder().response("Invalid email address.").build();
        }

        // Sprawdzenie minimalnej długości hasła
        if (request.getPassword().length() < 5) {
            return AuthenticationResponse.builder().response("Password must be at least 5 characters long.").build();
        }

        // Weryfikacja istnienia użytkownika o podanym adresie e-mail w bazie danych.
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().response("User with given e-mail already exists.").build();
        }

        var user = User.builder()
                .level(1)
                .points(0)
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response("User added successfully.").build();
    }
    private boolean isValidEmail(String email) {
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Verify the existence of a user by email address in the database.
        if (repository.findByEmail(request.getEmail()).isEmpty()) {
            return AuthenticationResponse.builder().response("User with given e-mail does not exist.").build();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        tokenInstance.setToken(request.getEmail());
        tokenInstance.setUserName(user.getUsername());
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public UserLVLResponse userLevel(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        int nextLVLPoints = calculateNextLVLPoints(user.getPoints(), user.getLevel());
        return UserLVLResponse.builder()
                .level(user.getLevel())
                .points(user.getPoints())
                .nextLVLPoints(nextLVLPoints)
                .build();
    }


    public UserLVLResponse sendPoints(PointsRequest pointsRequest) {
        var userEmail = tokenInstance.getToken();
        var user = repository.findByEmail(userEmail).orElseThrow();

        user.setPoints(user.getPoints() + pointsRequest.getPoints());
        updateLevel(user);

        repository.save(user);

        int nextLVLPoints = calculateNextLVLPoints(user.getPoints(), user.getLevel());

        return UserLVLResponse.builder()
                .level(user.getLevel())
                .points(user.getPoints())
                .nextLVLPoints(nextLVLPoints)
                .build();
    }


    private int calculateNextLVLPoints(int currentPoints, int currentLevel) {
        int requiredPoints = calculateRequiredPoints(currentLevel);
        return (currentLevel + 1) * requiredPoints;
    }

    private int calculateRequiredPoints(int currentLevel) {
        return 0 + (currentLevel * 50);
    }

    private void updateLevel(User user) {
        int requiredPoints = calculateRequiredPoints(user.getLevel());
        int nextLVLPoints = (user.getLevel() + 1) * requiredPoints;

        if (user.getPoints() >= nextLVLPoints) {
            user.setLevel(user.getLevel() + 1);
        }
    }

    public UserInfoResponse getInfo(String userEmail) {
        var user = repository.findByEmail(userEmail).orElseThrow();
        return UserInfoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .points(user.getPoints())
                .level(user.getLevel())
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest request) {
        var userEmail = tokenInstance.getToken();

        // Sprawdzenie pustego pola nowego hasła
        if (request.getNew_password().isEmpty()) {
            return AuthenticationResponse.builder().response("New password cannot be empty.").build();
        }

        // Pobranie użytkownika z bazy danych
        Optional<User> optionalUser = repository.findByEmail(userEmail);

        // Sprawdzenie, czy użytkownik istnieje
        if (optionalUser.isEmpty()) {
            return AuthenticationResponse.builder().response("User not found.").build();
        }

        var user = optionalUser.get();

        // Weryfikacja poprawności bieżącego hasła
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthenticationResponse.builder().response("Current password is incorrect.").build();
        }

        // Sprawdzenie, czy nowe hasło różni się od starego
        if (request.getNew_password().equals(request.getPassword())) {
            return AuthenticationResponse.builder().response("New password must be different from the current password.").build();
        }

        // Sprawdzenie minimalnej długości nowego hasła
        if (request.getNew_password().length() < 5) {
            return AuthenticationResponse.builder().response("New password must be at least 5 characters long.").build();
        }

        // Weryfikacja, czy nowe hasła się zgadzają
        if (!request.getNew_password().equals(request.getRe_new_password())) {
            return AuthenticationResponse.builder().response("New passwords do not match.").build();
        }

        // Aktualizacja hasła użytkownika
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);

        return AuthenticationResponse.builder().response("Password changed successfully.").build();
    }


    public AuthenticationResponse deleteUser(String userEmail, String userPassword) {
        var user = repository.findByEmail(userEmail).orElseThrow();

        // Weryfikacja hasła użytkownika przed usunięciem konta
        if (!passwordEncoder.matches(userPassword, user.getPassword())) {
            return AuthenticationResponse.builder().response("Incorrect password. User not deleted.").build();
        }

        repository.delete(user);
        return AuthenticationResponse.builder().response("User deleted successfully.").build();
    }
}
