package com.example.Fiszki.flashcards;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcards.flashcard.Flashcard;
import com.example.Fiszki.flashcards.flashcard.FlashcardRepository;
import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private TokenInstance tokenInstance = TokenInstance.getInstance();

    public FlashcardService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public FlashcardInfoResponse addFlashcard(FlashcardAddRequest request) {

        // Sprawdź, czy wszystkie wymagane pola są ustawione
        if (isNullOrEmpty(request.getCollectionName()) ||
                isNullOrEmpty(request.getCategory()) || isNullOrEmpty(request.getWord()) ||
                isNullOrEmpty(request.getTranslatedWord()) || isNullOrEmpty(request.getExample()) ||
                isNullOrEmpty(request.getTranslatedExample())) {
            return FlashcardInfoResponse.builder().response("All fields must be filled").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym słowie
        if (flashcardRepository.existsByWord(request.getWord())) {
            return FlashcardInfoResponse.builder().response("Flashcard with the given word already exists").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
        if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
            return FlashcardInfoResponse.builder().response("Flashcard with the given translated word already exists").build();
        }

        // Sprawdź, czy w polach collectionName, language, category, word i translatedWord występuje tylko jedno słowo
        if (!isSingleWord(request.getCollectionName()) ||
                !isSingleWord(request.getCategory())) {
            return FlashcardInfoResponse.builder().response("Fields not a single word").build();
        }

        Flashcard flashcard = Flashcard.builder()
                .collectionName(request.getCollectionName())
                .category(request.getCategory())
                .word(request.getWord())
                .translatedWord(request.getTranslatedWord())
                .example(request.getExample())
                .translatedExample(request.getTranslatedExample())
                .author(tokenInstance.getUserName())
                .build();

        flashcardRepository.save(flashcard);

        return FlashcardInfoResponse.builder().response("Flashcard added successfully").build();
    }

    public FlashcardInfoResponse editFlashcard(Integer flashcardId, FlashcardAddRequest request) {
        // Sprawdź, czy wszystkie wymagane pola są ustawione
        if (!isSingleWord(request.getCollectionName())) {
            return FlashcardInfoResponse.builder().response("All fields must be filled").build();
        }

        // Sprawdź, czy w polach word i translatedWord występuje tylko jedno słowo
        if (!isSingleWord(request.getWord()) || !isSingleWord(request.getTranslatedWord())) {
            return FlashcardInfoResponse.builder().response("Fields word and translatedWord must contain a single word").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym słowie
        if (flashcardRepository.existsByWord(request.getWord())) {
            return FlashcardInfoResponse.builder().response("Flashcard with the given word already exists").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
        if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
            return FlashcardInfoResponse.builder().response("Flashcard with the given translated word already exists").build();
        }

        Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            Flashcard flashcard = flashcardOptional.get();

            flashcard.setCollectionName(request.getCollectionName());
            flashcard.setCategory(request.getCategory());
            flashcard.setWord(request.getWord());
            flashcard.setTranslatedWord(request.getTranslatedWord());
            flashcard.setExample(request.getExample());
            flashcard.setTranslatedExample(request.getTranslatedExample());

            flashcardRepository.save(flashcard);

            return FlashcardInfoResponse.builder().response("Flashcard updated successfully").build();
        } else {
            return FlashcardInfoResponse.builder().response("Flashcard not found").build();
        }
    }

    //Funkcja sprawdza czy napis jest pusty lub null
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Funkcja sprawdza czy dany napis zawiera tylko jedno słowo
    private boolean isSingleWord(String str) {
        return str != null && str.trim().split("\\s+").length == 1;
    }


    public FlashcardReturnResponse showFlashcardById(Integer flashcardId) {
        // Tutaj możesz wykorzystać metody z FlashcardRepository do pobrania fiszki na podstawie ID
        Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            Flashcard flashcard = flashcardOptional.get();
            return FlashcardReturnResponse.builder()
                    .id(flashcard.getId())
                    .category(flashcard.getCategory())
                    .author(flashcard.getAuthor())
                    .word(flashcard.getWord())
                    .translatedWord(flashcard.getTranslatedWord())
                    .example(flashcard.getExample())
                    .translatedExample(flashcard.getTranslatedExample())
                    .build();
        } else {
            return FlashcardReturnResponse.builder().build();
        }
    }

    public FlashcardInfoResponse deleteFlashcardById(Integer flashcardId) {
        Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            flashcardRepository.deleteById(flashcardId);
            return FlashcardInfoResponse.builder()
                    .response("Flashcard deleted successfully.")
                    .build();
        } else {
            return FlashcardInfoResponse.builder()
                    .response("Flashcard not found or could not be deleted.")
                    .build();
        }
    }
    public List<FlashcardReturnResponse> showFlashcardsByCategory(String category) {
        List<Flashcard> flashcards = flashcardRepository.findByCategory(category);

        return flashcards.stream()
                .map(flashcard -> FlashcardReturnResponse.builder()
                        .id(flashcard.getId())
                        .category(flashcard.getCategory())
                        .author(flashcard.getAuthor())
                        .word(flashcard.getWord())
                        .translatedWord(flashcard.getTranslatedWord())
                        .example(flashcard.getExample())
                        .translatedExample(flashcard.getTranslatedExample())
                        .build())
                .collect(Collectors.toList());
    }

    public List<FlashcardReturnResponse> showFlashcardsByCategoryWithLimit(FlashcardCategoryLimitRequest request, String category) {
        List<Flashcard> flashcards = flashcardRepository.findByCategory(category);
        Collections.shuffle(flashcards);

        int limit = request.getLimit();
        if (limit > 0 && limit < flashcards.size()) {
            flashcards = flashcards.subList(0, limit);
        }

        return flashcards.stream()
                .map(flashcard -> FlashcardReturnResponse.builder()
                        .id(flashcard.getId())
                        .category(flashcard.getCategory())
                        .author(flashcard.getAuthor())
                        .word(flashcard.getWord())
                        .translatedWord(flashcard.getTranslatedWord())
                        .example(flashcard.getExample())
                        .translatedExample(flashcard.getTranslatedExample())
                        .build())
                .collect(Collectors.toList());
    }

    public List<FlashcardCollectionResponse> showAllCollection() {
        String author = tokenInstance.getUserName();
        List<Flashcard> flashcardAdds = flashcardRepository.findByAuthor(author);

        // Group flashcards by collection name
        return flashcardAdds.stream()
                .collect(Collectors.groupingBy(Flashcard::getCollectionName))
                .entrySet().stream()
                .map(entry -> {
                    String collectionName = entry.getKey();
                    List<FlashcardReturnResponse> flashcards = entry.getValue().stream()
                            .map(flashcard -> FlashcardReturnResponse.builder()
                                    .id(flashcard.getId())
                                    .category(flashcard.getCategory())
                                    .author(flashcard.getAuthor())
                                    .word(flashcard.getWord())
                                    .translatedWord(flashcard.getTranslatedWord())
                                    .example(flashcard.getExample())
                                    .translatedExample(flashcard.getTranslatedExample())
                                    .build())
                            .collect(Collectors.toList());

                    return FlashcardCollectionResponse.builder()
                            .name_kit(collectionName)
                            .flashcards(flashcards)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<FlashcardReturnResponse> showCollectionByName(String nameCollection) {
        String author = tokenInstance.getUserName();
        List<FlashcardReturnResponse> flashcards = flashcardRepository.findByCollectionNameAndAuthor(nameCollection, author)
                .stream()
                .map(flashcard -> FlashcardReturnResponse.builder()
                        .id(flashcard.getId())
                        .category(flashcard.getCategory())
                        .author(flashcard.getAuthor())
                        .word(flashcard.getWord())
                        .translatedWord(flashcard.getTranslatedWord())
                        .example(flashcard.getExample())
                        .translatedExample(flashcard.getTranslatedExample())
                        .build())
                .collect(Collectors.toList());

        return flashcards;
    }


    public List<Map<String, Object>> showCollectionInfo() {
        String author = tokenInstance.getUserName();
        List<Flashcard> flashcards = flashcardRepository.findByAuthor(author);

        return flashcards.stream()
                .collect(Collectors.groupingBy(Flashcard::getCollectionName))
                .entrySet().stream()
                .map(entry -> {
                    String collectionName = entry.getKey();
                    long flashcardsCount = entry.getValue().size();

                    Map<String, Object> collectionInfo = new HashMap<>();
                    collectionInfo.put("nameCollection", collectionName);
                    collectionInfo.put("flashcards", flashcardsCount);

                    return collectionInfo;
                })
                .collect(Collectors.toList());
    }

    public FlashcardInfoResponse deleteCollectionByName(String nameCollection) {
        String author = tokenInstance.getUserName();
        List<Flashcard> flashcardsToDelete = flashcardRepository.findByCollectionNameAndAuthor(nameCollection, author);

        if (!flashcardsToDelete.isEmpty()) {
            // Delete all flashcards from the collection created by the author
            flashcardRepository.deleteAll(flashcardsToDelete);

            return FlashcardInfoResponse.builder().response("The collection was successfully deleted").build();
        } else {
            return FlashcardInfoResponse.builder().response("Failure to delete the collection").build();
        }
    }
}
