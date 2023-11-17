package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.config.JwtService;
import com.example.Fiszki.security.user.Role;
import com.example.Fiszki.security.user.User;
import com.example.Fiszki.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private TokenInstance tokenInstance = TokenInstance.getInstance();
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        // Verify the existence of a user by email address in the database.
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().response("User with given e-mail already exist.").build();
        }

        var user = User.builder()
                .level(0)
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
        var userResponse = UserResponse.builder()
                .level(user.getLevel())
                .points(user.getPoints())
                .build();
        return UserLVLResponse.builder().userResponse(userResponse).build();
    }

    public UserLVLResponse sendPoints(PointsRequest pointsRequest) {
        var userEmail = tokenInstance.getToken(); // Assuming you have the user's email in the token
        var user = repository.findByEmail(userEmail).orElseThrow();

        // Add points to the user
        user.setPoints(user.getPoints() + pointsRequest.getPoints());

        // Update user level based on the points
        updateLevel(user);

        repository.save(user);

        var userResponse = UserResponse.builder()
                .level(user.getLevel())
                .points(user.getPoints())
                .build();
        return UserLVLResponse.builder().userResponse(userResponse).build();
    }

    private void updateLevel(User user) {
        int requiredPoints = 200;
        int parsedRequiredPoints = requiredPoints;
        int parsedLevel = user.getLevel();
        int points = user.getPoints();

        while (points >= parsedRequiredPoints) {
            parsedLevel++;
            points -= parsedRequiredPoints;
            parsedRequiredPoints += 200;
        }

        user.setLevel(parsedLevel);
        user.setPoints(points);
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
}
