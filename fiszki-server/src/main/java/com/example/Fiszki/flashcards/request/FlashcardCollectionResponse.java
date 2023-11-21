package com.example.Fiszki.flashcards.request;

import com.example.Fiszki.flashcards.response.FlashcardShowResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class FlashcardCollectionResponse {
    private String name_kit;
    private List<FlashcardShowResponse> flashcards;
}
