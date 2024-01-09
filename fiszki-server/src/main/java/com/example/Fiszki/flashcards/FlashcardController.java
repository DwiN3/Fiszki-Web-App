package com.example.Fiszki.flashcards;

import com.example.Fiszki.flashcards.flashcard.ApiError;
import com.example.Fiszki.flashcards.request.CollectionAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import com.example.Fiszki.security.auth.response.OtherException;
import com.example.Fiszki.security.auth.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/flashcards/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FlashcardController {
    private final FlashcardService flashcardService;

    @PostMapping("/add-flashcard")
    public ResponseEntity<?> addFlashcard(@RequestBody FlashcardAddRequest request) {
        try {
            FlashcardReturnResponse response = flashcardService.addFlashcard(request);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }

    @PutMapping("/edit/{flashcardsId}")
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
    public ResponseEntity<?> showFlashcard(@PathVariable Integer flashcardsId) {
        try {
            FlashcardReturnResponse response = flashcardService.showFlashcardById(flashcardsId);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserInfoResponse.builder().response("Flashcard with given id does not exist.").build());
            }
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
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
    public ResponseEntity<?> showFlashcardsByCategory(@PathVariable String category) {
        try {
            List<FlashcardReturnResponse> response = flashcardService.showFlashcardsByCategory(category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", e.getMessage());
            return ResponseEntity.status(apiError.getStatus()).body(apiError);
        }
    }

    @GetMapping("/category-limit/{category}")
    public ResponseEntity<?> showFlashcardsByCategoryWithLimit(@RequestBody FlashcardCategoryLimitRequest request, @PathVariable String category) {
        try {
            List<FlashcardReturnResponse> response = flashcardService.showFlashcardsByCategoryWithLimit(request, category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", e.getMessage());
            return ResponseEntity.status(apiError.getStatus()).body(apiError);
        }
    }

    @GetMapping("/collections")
    public ResponseEntity<?> showAllCollection() {
        try {
            List<FlashcardCollectionResponse> collections = flashcardService.showAllCollection();
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", e.getMessage());
            return ResponseEntity.status(apiError.getStatus()).body(apiError);
        }
    }

    @GetMapping("/collections-info")
    public ResponseEntity<?> showCollectionInfo() {
        try {
            List<Map<String, Object>> collections = flashcardService.showCollectionInfo();
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", e.getMessage());
            return ResponseEntity.status(apiError.getStatus()).body(apiError);
        }
    }

    @GetMapping("/collection/{nameCollection}")
    public ResponseEntity<?> showCollectionByName(@PathVariable String nameCollection) {
        try {
            List<FlashcardReturnResponse> collections = flashcardService.showCollectionByName(nameCollection);
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred", e.getMessage());
            return ResponseEntity.status(apiError.getStatus()).body(apiError);
        }
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

    @PostMapping("/add_collection")
    public ResponseEntity<FlashcardInfoResponse> addCollection(@RequestBody CollectionAddRequest request) {
        try {

            FlashcardInfoResponse response = flashcardService.addCollection(request);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.badRequest().body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }
}
