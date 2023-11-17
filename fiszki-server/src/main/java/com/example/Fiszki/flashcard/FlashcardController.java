package com.example.Fiszki.flashcard;

import com.example.Fiszki.flashcard.add.*;
import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashcards/")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @PostMapping("/add-flashcard")
    public ResponseEntity<FlashcardAddResponse> addFlashcard (@RequestBody FlashcardAddRequest request) {
        return ResponseEntity.ok(flashcardService.addFlashcard(request));
    }

    @GetMapping("/show/{flashcardsId}")
    public ResponseEntity<FlashcardShowResponse> showFlashcard(@PathVariable Integer flashcardsId) {
        return ResponseEntity.ok(flashcardService.showFlashcardById(flashcardsId));
    }

    @DeleteMapping("/delete/{flashcardsId}")
    public ResponseEntity<String> deleteFlashcardById(@PathVariable Integer flashcardsId) {
        try {
            flashcardService.deleteFlashcardById(flashcardsId);
            return ResponseEntity.ok("Flashcard deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting flashcard: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FlashcardShowResponse>> showFlashcardsByCategory(@PathVariable String category) {
        List<FlashcardShowResponse> flashcards = flashcardService.showFlashcardsByCategory(category);
        return ResponseEntity.ok(flashcards);
    }
}