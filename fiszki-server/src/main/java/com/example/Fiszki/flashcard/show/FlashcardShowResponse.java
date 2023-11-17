package com.example.Fiszki.flashcard.show;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardShowResponse {
    private Integer id;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;
    private String author;
}
