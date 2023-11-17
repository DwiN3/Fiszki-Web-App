package com.example.Fiszki.flashcard.collection;

import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class FlashcardCollectionResponse {
    private String name_kit;
    private List<FlashcardShowResponse> flashcards;
}
