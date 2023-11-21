package com.example.Fiszki.security.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDateResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private int points;
    private int level;
}
