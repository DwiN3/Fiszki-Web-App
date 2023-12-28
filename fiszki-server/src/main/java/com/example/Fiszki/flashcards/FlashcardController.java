package com.example.Fiszki.flashcards;

import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import com.example.Fiszki.security.auth.response.OtherException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<FlashcardInfoResponse> addFlashcard(@RequestBody FlashcardAddRequest request) {
        try {
            FlashcardInfoResponse response = flashcardService.addFlashcard(request);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }

    @PostMapping("/edit/{flashcardsId}")
    public ResponseEntity<FlashcardInfoResponse> editFlashcard(@PathVariable Integer flashcardsId, @RequestBody FlashcardAddRequest request) {
        try {
            FlashcardInfoResponse response = flashcardService.editFlashcard(flashcardsId, request);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response( e.getMessage()).build());
        }
    }

    @GetMapping("/show/{flashcardsId}")
    public ResponseEntity<FlashcardReturnResponse> showFlashcard(@PathVariable Integer flashcardsId) {
        return ResponseEntity.ok(flashcardService.showFlashcardById(flashcardsId));
    }

    @DeleteMapping("/delete/{flashcardId}")
    public ResponseEntity<FlashcardInfoResponse> deleteFlashcardById(@PathVariable Integer flashcardId) {
        try {
            FlashcardInfoResponse response = flashcardService.deleteFlashcardById(flashcardId);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
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
        List<FlashcardCollectionResponse> list = flashcardService.showAllCollection();
        System.out.println(list);
        return ResponseEntity.ok(list);
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
        try {
            FlashcardInfoResponse response = flashcardService.deleteCollectionByName(nameCollection);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }
}