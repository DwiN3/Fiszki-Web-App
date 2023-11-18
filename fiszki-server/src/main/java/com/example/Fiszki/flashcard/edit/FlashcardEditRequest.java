package com.example.Fiszki.flashcard.edit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardEditRequest {
    private String collectionName;
    private String category;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;
}
