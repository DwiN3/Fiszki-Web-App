package com.example.Fiszki.flashcard;

import com.example.Fiszki.flashcard.add.*;
import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import com.example.Fiszki.models.Flashcards;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashcards/")
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

    @GetMapping("/{flashcardsId}")
    public ResponseEntity<FlashcardShowResponse> showFlashcard(@PathVariable Integer flashcardsId) {
        return ResponseEntity.ok(flashcardService.showFlashcardById(flashcardsId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FlashcardShowResponse>> showFlashcardsByCategory(@PathVariable String category) {
        List<FlashcardShowResponse> flashcards = flashcardService.showFlashcardsByCategory(category);
        return ResponseEntity.ok(flashcards);
    }

    // PRZYKŁAD: Call<FlashcardID> deleteFlashcards(@Path("flashcardsId") String flashcardsId);
}