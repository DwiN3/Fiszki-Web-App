package com.example.Fiszki.flashcards.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class FlashcardCollectionResponse {
    private String kitName;
    private List<FlashcardReturnResponse> flashcards;
}
