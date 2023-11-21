package com.example.Fiszki.flashcards.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardReturnResponse {
    private Integer id;
    private String author;
    private String category;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;
}
