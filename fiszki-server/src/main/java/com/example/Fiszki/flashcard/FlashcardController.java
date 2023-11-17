package com.example.Fiszki.flashcard;

import com.example.Fiszki.models.Flashcards;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fiszki/")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService; // Dodaj pole FlashcardService

    Flashcards fiszka = new Flashcards("1","english", "category","kot",  "cat", "kot lubi mleko", "cat like milk");

    @GetMapping("/show-flashcard")
    public ResponseEntity<String> showFlashcard() {
        return ResponseEntity.ok("Twoja fiszka to: "+fiszka.getWord() + " tłumaczenie "+ fiszka.getTranslatedWord());
    }

    @PostMapping("/add-flashcard")
    public ResponseEntity<FlashcardResponse> addFlashcard (@RequestBody FlashcardRequest request) {
        return ResponseEntity.ok(flashcardService.addFlashcard(request));
    }
}