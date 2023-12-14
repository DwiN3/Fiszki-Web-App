package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.request.*;
import com.example.Fiszki.security.auth.response.*;
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

    public UserInfoResponse register(RegisterRequest request) {
        // Sprawdzenie pustych pól
//        if (request.getFirstname().isEmpty() || request.getLastname().isEmpty() ||
//                request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
//            return UserInfoResponse.builder().response("All fields must be filled in.").build();
//        }
//
//        // Sprawdzenie poprawności adresu e-mail
//        if (!isValidEmail(request.getEmail())) {
//            return UserInfoResponse.builder().response("Invalid email address.").build();
//        }
//
//        // Sprawdzenie minimalnej długości hasła
//        if (request.getPassword().length() < 5) {
//            return UserInfoResponse.builder().response("Password must be at least 5 characters long.").build();
//        }
//
//        // Weryfikacja istnienia użytkownika o podanym adresie e-mail w bazie danych.
//        if (repository.findByEmail(request.getEmail()).isPresent()) {
//            return UserInfoResponse.builder().response("User with given e-mail already exists.").build();
//        }

        if (!isValidRegistrationRequest(request)) {
            return UserInfoResponse.builder().response("Invalid registration request.").build();
        }

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return UserInfoResponse.builder().response("User with given e-mail already exists.").build();
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
        return UserInfoResponse.builder().response("User added successfully.").build();
    }

    private boolean isValidRegistrationRequest(RegisterRequest request) {
        if (request.getFirstname().isEmpty() || request.getLastname().isEmpty() ||
                request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            return false;
        }

        // Sprawdzenie poprawności adresu e-mail
        if (!isValidEmail(request.getEmail())) {
            return false;
        }

        // Sprawdzenie minimalnej długości hasła
        return request.getPassword().length() >= 5;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    public UserInfoResponse authenticate(AuthenticationRequest request) {
        // Verify the existence of a user by email address in the database.
        var userOptional = repository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
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
            return UserInfoResponse.builder().response(jwtToken).build();
        } else {
            return null;
        }
    }

    // W AuthenticationService
    public UserLevelResponse userLevel() {
        var userEmail = TokenInstance.getInstance().getToken();
        var user = repository.findByEmail(userEmail).orElseThrow();
        int nextLVLPoints = calculateNextLVLPoints(user.getPoints(), user.getLevel());
        return UserLevelResponse.builder()
                .level(user.getLevel())
                .points(user.getPoints())
                .nextLVLPoints(nextLVLPoints)
                .build();
    }


    public UserLevelResponse sendPoints(PointsRequest pointsRequest) {
        var userEmail = tokenInstance.getToken();
        var user = repository.findByEmail(userEmail).orElseThrow();

        user.setPoints(user.getPoints() + pointsRequest.getPoints());
        updateLevel(user);

        repository.save(user);

        int nextLVLPoints = calculateNextLVLPoints(user.getPoints(), user.getLevel());

        return UserLevelResponse.builder()
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

    public UserDateResponse getInfo() {
        var userEmail = TokenInstance.getInstance().getToken();
        var user = repository.findByEmail(userEmail).orElseThrow();
        return UserDateResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .points(user.getPoints())
                .level(user.getLevel())
                .build();
    }

    public UserInfoResponse changePassword(ChangePasswordRequest request) {
        var userEmail = tokenInstance.getToken();

        // Sprawdzenie pustego pola nowego hasła
        if (request.getNew_password().isEmpty()) {
            return UserInfoResponse.builder().response("New password cannot be empty.").build();
        }

        // Pobranie użytkownika z bazy danych
        Optional<User> optionalUser = repository.findByEmail(userEmail);

        // Sprawdzenie, czy użytkownik istnieje
        if (optionalUser.isEmpty()) {
            return UserInfoResponse.builder().response("User not found.").build();
        }

        var user = optionalUser.get();

        // Weryfikacja poprawności bieżącego hasła
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return UserInfoResponse.builder().response("Current password is incorrect.").build();
        }

        // Sprawdzenie, czy nowe hasło różni się od starego
        if (request.getNew_password().equals(request.getPassword())) {
            return UserInfoResponse.builder().response("New password must be different from the current password.").build();
        }

        // Sprawdzenie minimalnej długości nowego hasła
        if (request.getNew_password().length() < 5) {
            return UserInfoResponse.builder().response("New password must be at least 5 characters long.").build();
        }

        // Weryfikacja, czy nowe hasła się zgadzają
        if (!request.getNew_password().equals(request.getRe_new_password())) {
            return UserInfoResponse.builder().response("New passwords do not match.").build();
        }

        // Aktualizacja hasła użytkownika
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);

        return UserInfoResponse.builder().response("Password changed successfully.").build();
    }

    public UserInfoResponse changePasswordLink(ChangePasswordFromLinkRequest request) {
        // Verify the existence of a user by email address in the database.
        Optional<User> optionalUser = repository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return UserInfoResponse.builder().response("User not found.").build();
        }

        var user = optionalUser.get();

        // Validate the new password fields
        if (request.getNew_password().isEmpty() || request.getNew_password().length() < 5) {
            return UserInfoResponse.builder().response("Invalid new password.").build();
        }

        if (!request.getNew_password().equals(request.getRe_new_password())) {
            return UserInfoResponse.builder().response("New passwords do not match.").build();
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);

        return UserInfoResponse.builder().response("Password changed successfully.").build();
    }



    public UserInfoResponse deleteUser(String userPassword) {
        var userEmail = tokenInstance.getToken();
        var user = repository.findByEmail(userEmail).orElseThrow();

        // Weryfikacja hasła użytkownika przed usunięciem konta
        if (!passwordEncoder.matches(userPassword, user.getPassword())) {
            return UserInfoResponse.builder().response("Incorrect password. User not deleted.").build();
        }

        repository.delete(user);
        return UserInfoResponse.builder().response("User deleted successfully.").build();
    }

    public TokenValidityResponse checkAccess(TokenValidityRequest tokenValidityRequest) {
        String token = tokenValidityRequest.getToken();

        // Verify the token using JwtService
        boolean isValidToken = jwtService.validateToken(token);

        // Use a boolean field in TokenValidityResponse to represent access
        return TokenValidityResponse.builder().access(isValidToken).build();
    }
}
