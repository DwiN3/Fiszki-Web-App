package com.example.Fiszki.flashcards;

import com.example.Fiszki.flashcards.flashcard.FlashcardCollection;
import com.example.Fiszki.flashcards.request.CollectionAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardCollectionRepository;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import com.example.Fiszki.security.auth.response.OtherException;
import com.example.Fiszki.security.auth.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ch.qos.logback.core.util.OptionHelper.isNullOrEmpty;


@RestController
@RequestMapping("/flashcards/")
@RequiredArgsConstructor
public class FlashcardController {
    private final FlashcardService flashcardService;
    private final FlashcardCollectionRepository collectionRepository;


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
    public ResponseEntity<List<FlashcardReturnResponse>> showFlashcardsByCategory(@PathVariable String category) {
        try {
            List<FlashcardReturnResponse> response = flashcardService.showFlashcardsByCategory(category);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }

    @GetMapping("/category-limit/{category}")
    public ResponseEntity<List<FlashcardReturnResponse>> showFlashcardsByCategoryWithLimit(@RequestBody FlashcardCategoryLimitRequest request, @PathVariable String category) {
        try {
            List<FlashcardReturnResponse> response = flashcardService.showFlashcardsByCategoryWithLimit(request,category);
            return ResponseEntity.ok(response);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }

    @GetMapping("/collections")
    public ResponseEntity<List<FlashcardCollectionResponse>> showAllCollection() {
        try {
            List<FlashcardCollectionResponse> collections = flashcardService.showAllCollection();
            return ResponseEntity.ok(collections);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((List<FlashcardCollectionResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((List<FlashcardCollectionResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }

    @GetMapping("/collections-info")
    public ResponseEntity<List<Map<String, Object>>> showCollectionInfo() {
        try {
            List<Map<String, Object>> collections = flashcardService.showCollectionInfo();
            return ResponseEntity.ok(collections);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/collection/{nameCollection}")
    public ResponseEntity<List<FlashcardReturnResponse>> showCollectionByName(@PathVariable String nameCollection) {
        try {
            List<FlashcardReturnResponse> collections = flashcardService.showCollectionByName(nameCollection);
            return ResponseEntity.ok(collections);
        } catch (OtherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((List<FlashcardReturnResponse>) FlashcardInfoResponse.builder().response(e.getMessage()).build());
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
            // Sprawdź, czy nazwa kolekcji nie jest pusta
            if (isNullOrEmpty(request.getCollectionName())) {
                throw new OtherException("Collection name must be provided");
            }

            // Sprawdź, czy kolekcja o podanej nazwie już istnieje
            if (collectionRepository.existsByCollectionName(request.getCollectionName())) {
                throw new OtherException("Collection with the given name already exists");
            }

            // Utwórz nową kolekcję
            FlashcardCollection newCollection = FlashcardCollection.builder()
                    .collectionName(request.getCollectionName())
                    .build();

            collectionRepository.save(newCollection);

            return ResponseEntity.ok(FlashcardInfoResponse.builder().response("Collection added successfully").build());
        } catch (OtherException e) {
            return ResponseEntity.badRequest().body(FlashcardInfoResponse.builder().response(e.getMessage()).build());
        }
    }
}
