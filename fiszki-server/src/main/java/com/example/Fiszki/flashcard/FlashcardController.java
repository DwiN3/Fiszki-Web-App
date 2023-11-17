package com.example.Fiszki.flashcard;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcard.add.*;
import com.example.Fiszki.flashcard.collection.FlashcardCollectionResponse;
import com.example.Fiszki.flashcard.edit.FlashcardEditRequest;
import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flashcards/")
@RequiredArgsConstructor
public class FlashcardController {
    TokenInstance tokenInstance = TokenInstance.getInstance();

    private final FlashcardService flashcardService;

    @PostMapping("/add-flashcard")
    public ResponseEntity<FlashcardAddResponse> addFlashcard (@RequestBody FlashcardAddRequest request) {
        return ResponseEntity.ok(flashcardService.addFlashcard(request));
    }

    @PostMapping("/edit/{flashcardsId}")
    public ResponseEntity<FlashcardAddResponse> editFlashcard(@PathVariable Integer flashcardsId,
                                                              @RequestBody FlashcardEditRequest request) {
        FlashcardAddResponse response = flashcardService.editFlashcard(flashcardsId, request);
        return ResponseEntity.ok(response);
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

    @GetMapping("/collections")
    public ResponseEntity<List<FlashcardCollectionResponse>> showAllCollection() {
        String author = tokenInstance.getUserName();
        List<FlashcardCollectionResponse> collections = flashcardService.showAllCollection(author);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/collections-info")
    public ResponseEntity<List<Map<String, Object>>> showCollectionInfo() {
        String author = tokenInstance.getUserName();
        List<Map<String, Object>> collectionInfo = flashcardService.showCollectionInfo(author);
        return ResponseEntity.ok(collectionInfo);
    }

    @GetMapping("/collection/{nameCollection}")
    public ResponseEntity<List<FlashcardShowResponse>> showCollectionByName(@PathVariable String nameCollection) {
        String author = tokenInstance.getUserName();
        List<FlashcardShowResponse> flashcards = flashcardService.showCollectionByName(nameCollection, author);
        return ResponseEntity.ok(flashcards);
    }

    @DeleteMapping("/collection/{nameCollection}")
    public ResponseEntity<String> deleteCollectionByName(@PathVariable String nameCollection) {
        String author = tokenInstance.getUserName();
        try {
            flashcardService.deleteCollectionByName(nameCollection, author);
            return ResponseEntity.ok("Collection deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting collection: " + e.getMessage());
        }
    }
}