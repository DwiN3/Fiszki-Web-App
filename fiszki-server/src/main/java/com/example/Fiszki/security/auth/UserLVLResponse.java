package com.example.Fiszki.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLVLResponse {
    private int level;
    private int points;
    private int nextLVLPoints;
}
