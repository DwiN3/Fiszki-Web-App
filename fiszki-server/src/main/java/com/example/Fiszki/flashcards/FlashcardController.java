package com.example.Fiszki.flashcards;

import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flashcards/")
@RequiredArgsConstructor
public class FlashcardController {
    private final FlashcardService flashcardService;

    @PostMapping("/add-flashcard")
    public ResponseEntity<FlashcardInfoResponse> addFlashcard (@RequestBody FlashcardAddRequest request) {
        return ResponseEntity.ok(flashcardService.addFlashcard(request));
    }

    @PostMapping("/edit/{flashcardsId}")
    public ResponseEntity<FlashcardInfoResponse> editFlashcard(@PathVariable Integer flashcardsId, @RequestBody FlashcardAddRequest request) {
        return ResponseEntity.ok(flashcardService.editFlashcard(flashcardsId, request));
    }

    @GetMapping("/show/{flashcardsId}")
    public ResponseEntity<FlashcardReturnResponse> showFlashcard(@PathVariable Integer flashcardsId) {
        return ResponseEntity.ok(flashcardService.showFlashcardById(flashcardsId));
    }

    @DeleteMapping("/delete/{flashcardId}")
    public ResponseEntity<FlashcardInfoResponse> deleteFlashcardById(@PathVariable Integer flashcardId) {
        return ResponseEntity.ok(flashcardService.deleteFlashcardById(flashcardId));
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FlashcardReturnResponse>> showFlashcardsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(flashcardService.showFlashcardsByCategory(category));
    }

    @GetMapping("/category-limit/{category}")
    public ResponseEntity<List<FlashcardReturnResponse>> showFlashcardsByCategoryWithLimit(@RequestBody FlashcardCategoryLimitRequest request, @PathVariable String category) {
        return ResponseEntity.ok(flashcardService.showFlashcardsByCategoryWithLimit(request,category));
    }

    @GetMapping("/collections")
    public ResponseEntity<List<FlashcardCollectionResponse>> showAllCollection() {
        return ResponseEntity.ok(flashcardService.showAllCollection());
    }

    @GetMapping("/collections-info")
    public ResponseEntity<List<Map<String, Object>>> showCollectionInfo() {
        return ResponseEntity.ok(flashcardService.showCollectionInfo());
    }

    @GetMapping("/collection/{nameCollection}")
    public ResponseEntity<List<FlashcardReturnResponse>> showCollectionByName(@PathVariable String nameCollection) {
        return ResponseEntity.ok(flashcardService.showCollectionByName(nameCollection));
    }

    @DeleteMapping("/collection/{nameCollection}")
    public ResponseEntity<FlashcardInfoResponse> deleteCollectionByName(@PathVariable String nameCollection) {
        return ResponseEntity.ok(flashcardService.deleteCollectionByName(nameCollection));
    }
}