package com.example.Fiszki.flashcards.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest {
    private String collectionName;

    // pozostałe metody gettery, settery itp.
}
