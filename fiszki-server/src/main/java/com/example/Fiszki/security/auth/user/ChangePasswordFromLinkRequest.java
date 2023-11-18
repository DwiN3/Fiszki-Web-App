package com.example.Fiszki.security.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordFromLinkRequest {
    private String email;
    private String new_password;
    private String re_new_password;
}
