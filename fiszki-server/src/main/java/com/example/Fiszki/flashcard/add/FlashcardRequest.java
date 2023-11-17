package com.example.Fiszki.flashcard.add;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardRequest {
    private String collectionName;
    private String language;
    private String category;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;
}
