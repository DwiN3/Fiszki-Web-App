package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcard.add.FlashcardResponse;
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

        // Verify the existence of a user by email address in the database. - Extra
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().token("User with given e-mail already exist.").build();
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );
        //System.out.println("Mail: "+request.getEmail());
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        System.out.println("\n\n\n\nTOKEN "+jwtToken);
        tokenInstance.setToken(jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
