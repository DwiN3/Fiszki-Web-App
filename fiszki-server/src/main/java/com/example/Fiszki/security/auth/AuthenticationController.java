package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.request.*;
import com.example.Fiszki.security.auth.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;

import java.util.NoSuchElementException;

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
        } else if (response.getResponse().equals("Repeated password is different from the password.")) {
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserInfoResponse.builder().response("User with given e-mail does not exist.").build());
            }
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserInfoResponse.builder().response("Authentication error: " + e.getMessage()).build());
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        }
    }

    @GetMapping("/level")
    public ResponseEntity<?> userLevel() {
        try {
            UserLevelResponse userLevelResponse = authenticationService.userLevel();
            return ResponseEntity.ok(userLevelResponse);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserInfoResponse.builder().response("Access denied: " + e.getMessage()).build());
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("NoSuchElementException: " + "Invalid token").build());
        }
    }

    @PutMapping("/points")
    public ResponseEntity<?> sendPoints(@RequestBody PointsRequest pointsRequest) {
        try {
            UserLevelResponse userLevelResponse = authenticationService.sendPoints(pointsRequest);
            return ResponseEntity.ok(userLevelResponse);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserInfoResponse.builder().response("Access denied: " + e.getMessage()).build());
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserInfoResponse.builder().response("NoSuchElementException: " + "Invalid token").build());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        try {
            UserDateResponse userLevelResponse = authenticationService.getInfo();
            return ResponseEntity.ok(userLevelResponse);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserInfoResponse.builder().response("Access denied: " + e.getMessage()).build());
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserInfoResponse.builder().response("NoSuchElementException: " + "Invalid token").build());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            UserInfoResponse response = authenticationService.changePassword(request);
            if (response.getResponse().equals("User with given e-mail does not exist.")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (!response.getResponse().equals("Password changed successfully.")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        }
    }

    @PutMapping("/process-password-change")
    public ResponseEntity<UserInfoResponse> processPasswordChange(@RequestBody ChangePasswordFromLinkRequest request) {
        try {
            UserInfoResponse response = authenticationService.changePasswordLink(request);
            if (response.getResponse().equals("User with given e-mail does not exist.")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (!response.getResponse().equals("Password changed successfully.")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<UserInfoResponse> deleteUser(@RequestBody UserDeleteRequest request) {
        try {
            UserInfoResponse response = authenticationService.deleteUser(request.getPassword());
            if (response.getResponse().equals("Incorrect password. User not deleted.")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserInfoResponse.builder().response("NoSuchElementException: " + "Invalid token").build());
        } catch (UsernameNotFoundException e ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserInfoResponse.builder().response("User not found: " + e.getMessage()).build());
        }
    }

    @PostMapping("/access")
    public ResponseEntity<?> access(@RequestBody TokenValidityRequest tokenValidityRequest) {
        try {
            TokenValidityResponse response = authenticationService.checkAccess(tokenValidityRequest);
            if (!response.isAccess()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserInfoResponse.builder().response("Other error: " + e.getMessage()).build());
        }
    }
}
