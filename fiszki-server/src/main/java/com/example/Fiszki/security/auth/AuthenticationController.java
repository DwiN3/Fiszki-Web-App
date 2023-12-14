package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.request.*;
import com.example.Fiszki.security.auth.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/flashcards/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponse> register (@RequestBody RegisterRequest request) {
        UserInfoResponse response = authenticationService.register(request);
        if (response.getResponse().equals("Invalid registration request.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (response.getResponse().equals("User with given e-mail already exists.")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticate) {
        try {
            UserInfoResponse response = authenticationService.authenticate(authenticate);
            if (response == null) {
                // Zwrócenie informacji o braku użytkownika z podanym e-mail w bazie
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with given e-mail does not exist.");
            }
            // Pomyślne logowanie
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Obsługa błędu autentykacji
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication error: " + e.getMessage());
        } catch (OtherException e) {
            // Obsługa innych błędów
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Other error: " + e.getMessage());
        }
    }

    @GetMapping("/level")
    public ResponseEntity<UserLevelResponse> userLevel() {
        return ResponseEntity.ok(authenticationService.userLevel());
    }

    @PutMapping("/points")
    public ResponseEntity<UserLevelResponse> sendPoints(@RequestBody PointsRequest pointsRequest) {
        return ResponseEntity.ok(authenticationService.sendPoints(pointsRequest));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDateResponse> getUserInfo() {
        return ResponseEntity.ok(authenticationService.getInfo());
    }

    @PostMapping("/password-change")
    public ResponseEntity<UserInfoResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authenticationService.changePassword(request));
    }

    @PostMapping("/process-password-change")
    public ResponseEntity<UserInfoResponse> processPasswordChange(@RequestBody ChangePasswordFromLinkRequest request) {
        return ResponseEntity.ok(authenticationService.changePasswordLink(request));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<UserInfoResponse> deleteUser(@RequestBody UserDeleteRequest request) {
        return ResponseEntity.ok(authenticationService.deleteUser(request.getPassword()));
    }

    @PostMapping("/access")
    public ResponseEntity<TokenValidityResponse> access(@RequestBody TokenValidityRequest tokenValidityRequest) {
        return ResponseEntity.ok(authenticationService.checkAccess(tokenValidityRequest));
    }
}
