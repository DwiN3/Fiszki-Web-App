package com.example.Fiszki.security.auth;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.user.ChangePasswordRequest;
import com.example.Fiszki.security.auth.user.PointsRequest;
import com.example.Fiszki.security.auth.user.UserInfoResponse;
import com.example.Fiszki.security.auth.user.UserLVLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flashcards/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    TokenInstance tokenInstance = TokenInstance.getInstance();

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest authenticate) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticate));
    }

    @GetMapping("/level")
    public ResponseEntity<UserLVLResponse> userLevel() {
        var userEmail = TokenInstance.getInstance().getToken();
        var authenticationRequest = AuthenticationRequest.builder().email(userEmail).build();
        return ResponseEntity.ok(authenticationService.userLevel(authenticationRequest));
    }

    @PutMapping("/points")
    public ResponseEntity<UserLVLResponse> sendPoints(@RequestBody PointsRequest pointsRequest) {
        return ResponseEntity.ok(authenticationService.sendPoints(pointsRequest));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        var userEmail = tokenInstance.getToken();
        return ResponseEntity.ok(authenticationService.getInfo(userEmail));
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthenticationResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authenticationService.changePassword(request));
    }


    @DeleteMapping("/delete-user")
    public ResponseEntity<AuthenticationResponse> deleteUser() {
        var userEmail = tokenInstance.getToken();
        return ResponseEntity.ok(authenticationService.deleteUser(userEmail));
    }
}
