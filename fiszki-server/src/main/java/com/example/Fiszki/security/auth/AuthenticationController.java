package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.request.*;
import com.example.Fiszki.security.auth.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flashcards/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> authenticate (@RequestBody AuthenticationRequest authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
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
}
