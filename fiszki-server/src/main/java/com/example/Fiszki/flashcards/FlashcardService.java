package com.example.Fiszki.flashcards;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcards.flashcard.CollectionRepository;
import com.example.Fiszki.flashcards.flashcard.Flashcard;
import com.example.Fiszki.flashcards.flashcard.FlashcardCollection;
import com.example.Fiszki.flashcards.flashcard.FlashcardRepository;
import com.example.Fiszki.flashcards.request.FlashcardAddRequest;
import com.example.Fiszki.flashcards.request.FlashcardCategoryLimitRequest;
import com.example.Fiszki.flashcards.response.FlashcardCollectionResponse;
import com.example.Fiszki.flashcards.response.FlashcardInfoResponse;
import com.example.Fiszki.flashcards.response.FlashcardReturnResponse;
import com.example.Fiszki.security.auth.response.OtherException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final CollectionRepository collectionRepository;
    private TokenInstance tokenInstance = TokenInstance.getInstance();

    public FlashcardService(FlashcardRepository flashcardRepository, CollectionRepository collectionRepository) {
        this.flashcardRepository = flashcardRepository;
        this.collectionRepository = collectionRepository;
    }

    public FlashcardInfoResponse addFlashcard(FlashcardAddRequest request) throws OtherException {
        try {
            // Sprawdź, czy wszystkie wymagane pola są ustawione
            if (isNullOrEmpty(request.getCollectionName()) ||
                    isNullOrEmpty(request.getCategory()) || isNullOrEmpty(request.getWord()) ||
                    isNullOrEmpty(request.getTranslatedWord()) || isNullOrEmpty(request.getExample()) ||
                    isNullOrEmpty(request.getTranslatedExample())) {
                throw new OtherException("All fields must be filled");
            }

            // Sprawdź, czy istnieje fiszka o podanym słowie
            if (flashcardRepository.existsByWord(request.getWord())) {
                throw new OtherException("Flashcard with the given word already exists");
            }

            // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
            if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
                throw new OtherException("Flashcard with the given translated word already exists");
            }

            // Sprawdź, czy w polach collectionName, language, category, word i translatedWord występuje tylko jedno słowo
            if (!isSingleWord(request.getCollectionName()) || !isSingleWord(request.getCategory())) {
                throw new OtherException("Fields not a single word");
            }

            // Pobierz kolekcję na podstawie nazwy
            FlashcardCollection collection = collectionRepository.findByCollectionName(request.getCollectionName())
                    .orElseThrow(() -> new OtherException("Collection not found"));

            Flashcard flashcard = Flashcard.builder()
                    .collection(collection)
                    .category(request.getCategory())
                    .word(request.getWord())
                    .translatedWord(request.getTranslatedWord())
                    .example(request.getExample())
                    .translatedExample(request.getTranslatedExample())
                    .author(tokenInstance.getUserName())
                    .build();

            flashcardRepository.save(flashcard);

            return FlashcardInfoResponse.builder().response("Flashcard added successfully").build();
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }

    public FlashcardInfoResponse editFlashcard(Integer flashcardId, FlashcardAddRequest request) throws OtherException {
        try {
            // Sprawdź, czy wszystkie wymagane pola są ustawione
            if (!isSingleWord(request.getCollectionName())) {
                throw new OtherException("All fields must be filled");
            }
            // Sprawdź, czy w polach word i translatedWord występuje tylko jedno słowo
            if (!isSingleWord(request.getWord()) || !isSingleWord(request.getTranslatedWord())) {
                throw new OtherException("Fields word and translatedWord must contain a single word");
            }

            // Sprawdź, czy istnieje fiszka o podanym słowie
            if (flashcardRepository.existsByWord(request.getWord())) {
                throw new OtherException("Flashcard with the given word already exists");
            }
            // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
            if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
                throw new OtherException("Flashcard with the given translated word already exists");
            }
            Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);
            if (flashcardOptional.isPresent()) {
                Flashcard flashcard = flashcardOptional.get();

                flashcard.setCollection(FlashcardCollection.builder().collectionName(request.getCollectionName()).build());
                flashcard.setCategory(request.getCategory());
                flashcard.setWord(request.getWord());
                flashcard.setTranslatedWord(request.getTranslatedWord());
                flashcard.setExample(request.getExample());
                flashcard.setTranslatedExample(request.getTranslatedExample());
                flashcardRepository.save(flashcard);

                return FlashcardInfoResponse.builder().response("Flashcard updated successfully").build();
            } else {
                throw new OtherException("Flashcard not found");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
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
            return null;
        }
    }

    public FlashcardInfoResponse deleteFlashcardById(Integer flashcardId) throws OtherException {
        try {
            Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);

            if (flashcardOptional.isPresent()) {
                flashcardRepository.deleteById(flashcardId);
                return FlashcardInfoResponse.builder().response("Flashcard deleted successfully.").build();
            } else {
                throw new OtherException("Flashcard not found or could not be deleted.");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
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
                .collect(Collectors.groupingBy(flashcard -> flashcard.getCollection().getCollectionName()))
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
        List<Flashcard> flashcards = flashcardRepository.findByCollection_CollectionNameAndAuthor(nameCollection, author);

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


    public List<Map<String, Object>> showCollectionInfo() {
        String author = tokenInstance.getUserName();
        List<Flashcard> flashcards = flashcardRepository.findByAuthor(author);

        return flashcards.stream()
                .collect(Collectors.groupingBy(flashcard -> flashcard.getCollection().getCollectionName()))
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


    public FlashcardInfoResponse deleteCollectionByName(String nameCollection) throws OtherException {
        try {
            Optional<FlashcardCollection> collectionToDelete = collectionRepository.findByCollectionName(nameCollection);

            if (collectionToDelete.isPresent()) {
                FlashcardCollection collection = collectionToDelete.get();

                // Pobierz wszystkie fiszki z kolekcji
                List<Flashcard> flashcardsToDelete = flashcardRepository.findByCollection(collection);

                // Usuń wszystkie fiszki
                flashcardRepository.deleteAll(flashcardsToDelete);

                // Usuń kolekcję
                collectionRepository.delete(collection);

                return FlashcardInfoResponse.builder().response("Collection deleted successfully.").build();
            } else {
                throw new OtherException("Collection not found or could not be deleted.");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }
}
