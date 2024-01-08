package com.example.Fiszki.flashcards;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcards.flashcard.CollectionRepository;
import com.example.Fiszki.flashcards.flashcard.Flashcard;
import com.example.Fiszki.flashcards.flashcard.FlashcardCollection;
import com.example.Fiszki.flashcards.flashcard.FlashcardRepository;
import com.example.Fiszki.flashcards.request.CollectionAddRequest;
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

    public FlashcardReturnResponse addFlashcard(FlashcardAddRequest request) throws OtherException {
        try {
            // Sprawdź, czy wszystkie wymagane pola są ustawione
            if (isNullOrEmpty(request.getCollectionName()) ||
                    isNullOrEmpty(request.getCategory()) || isNullOrEmpty(request.getWord()) ||
                    isNullOrEmpty(request.getTranslatedWord()) || isNullOrEmpty(request.getExample()) ||
                    isNullOrEmpty(request.getTranslatedExample())) {
                throw new OtherException("All fields must be filled");
            }

//            // Sprawdź, czy istnieje fiszka o podanym słowie
//            if (flashcardRepository.existsByWord(request.getWord())) {
//                throw new OtherException("Flashcard with the given word already exists");
//            }
//
//            // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
//            if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
//                throw new OtherException("Flashcard with the given translated word already exists");
//            }

            // Sprawdź, czy istnieje fiszka o podanym słowie w konkretnej kolekcji
            if (flashcardRepository.existsByWordAndCollection_CollectionName(request.getWord(), request.getCollectionName())) {
                throw new OtherException("Flashcard with the given word already exists in this collection");
            }

            // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie w konkretnej kolekcji
            if (flashcardRepository.existsByTranslatedWordAndCollection_CollectionName(request.getTranslatedWord(), request.getCollectionName())) {
                throw new OtherException("Flashcard with the given translated word already exists in this collection");
            }

            // Sprawdź, czy w polach collectionName, language, category, word i translatedWord występuje tylko jedno słowo
            if (!isSingleWord(request.getCollectionName()) || !isSingleWord(request.getCategory())) {
                throw new OtherException("Fields not a single word");
            }

            // Pobierz kolekcję na podstawie nazwy i autora
            List<FlashcardCollection> collections = collectionRepository.findByCollectionNameAndAuthor(request.getCollectionName(), tokenInstance.getUserName());

            if (collections.isEmpty()) {
                // Obsłuż sytuację, gdy kolekcja nie istnieje
                throw new OtherException("Collection not found");
            }

            if (collections.size() > 1) {
                // Obsłuż sytuację, gdy istnieje więcej niż jedna kolekcja o danej nazwie dla tego autora
                throw new OtherException("Multiple collections with the same name exist for this user");
            }

            FlashcardCollection collection = collections.get(0);

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

            FlashcardReturnResponse flashcardReturnResponse = FlashcardReturnResponse.builder()
                    .id(flashcard.getId())
                    .category(flashcard.getCategory())
                    .author(flashcard.getAuthor())
                    .word(flashcard.getWord())
                    .translatedWord(flashcard.getTranslatedWord())
                    .example(flashcard.getExample())
                    .translatedExample(flashcard.getTranslatedExample())
                    .build();

            return flashcardReturnResponse;
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }

    public FlashcardInfoResponse addCollection(CollectionAddRequest request) throws OtherException {
        try {
            // Sprawdź, czy nazwa kolekcji nie jest pusta
            if (isNullOrEmpty(request.getCollectionName())) {
                throw new OtherException("Collection name must be provided");
            }

            // Sprawdź, czy kolekcja o podanej nazwie już istnieje
            if (collectionRepository.existsByCollectionNameAndAuthor(request.getCollectionName(), tokenInstance.getUserName())) {
                throw new OtherException("Collection with the given name already exists for this user");
            }

            // Utwórz nową kolekcję przypisaną do autora
            FlashcardCollection newCollection = FlashcardCollection.builder()
                    .collectionName(request.getCollectionName())
                    .author(tokenInstance.getUserName())
                    .build();

            collectionRepository.save(newCollection);

            return FlashcardInfoResponse.builder().response("Collection added successfully").build();
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

                // Pobierz kolekcję na podstawie nazwy i autora
                List<FlashcardCollection> collections = collectionRepository.findByCollectionNameAndAuthor(request.getCollectionName(), tokenInstance.getUserName());

                if (collections.isEmpty()) {
                    // Obsłuż sytuację, gdy kolekcja nie istnieje
                    throw new OtherException("Collection not found");
                }

                if (collections.size() > 1) {
                    // Obsłuż sytuację, gdy istnieje więcej niż jedna kolekcja o danej nazwie dla tego autora
                    throw new OtherException("Multiple collections with the same name exist for this user");
                }

                FlashcardCollection collection = collections.get(0);

                // Aktualizuj fiszkę
                flashcard.setCollection(collection);
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
        // Pobranie fiszki na podstawie ID
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
        List<Flashcard> flashcards = flashcardRepository.findByAuthor(author);
        List<FlashcardCollection> flashcardCollections = collectionRepository.findCollectionByAuthor(author);

        // Group flashcards by collection name
        Map<String, List<FlashcardReturnResponse>> groupedFlashcards = flashcards.stream()
                .collect(Collectors.groupingBy(flashcard -> {
                            FlashcardCollection collection = flashcard.getCollection();
                            return (collection != null) ? collection.getCollectionName() : null;
                        },
                        Collectors.mapping(flashcard -> FlashcardReturnResponse.builder()
                                .id(flashcard.getId())
                                .category(flashcard.getCategory())
                                .author(flashcard.getAuthor())
                                .word(flashcard.getWord())
                                .translatedWord(flashcard.getTranslatedWord())
                                .example(flashcard.getExample())
                                .translatedExample(flashcard.getTranslatedExample())
                                .build(), Collectors.toList())));

        // Get all collection names from flashcardCollections
        Set<String> allCollectionNames = flashcardCollections.stream()
                .map(FlashcardCollection::getCollectionName)
                .collect(Collectors.toSet());

        // Create FlashcardCollectionResponse for each collection (including empty ones)
        return allCollectionNames.stream()
                .map(collectionName -> FlashcardCollectionResponse.builder()
                        .name_kit(collectionName)
                        .flashcards(groupedFlashcards.getOrDefault(collectionName, Collections.emptyList()))
                        .build())
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
        List<FlashcardCollection> flashcardCollections = collectionRepository.findCollectionByAuthor(author);

        Map<String, Long> flashcardsCountByCollection = flashcards.stream()
                .collect(Collectors.groupingBy(flashcard -> {
                    FlashcardCollection collection = flashcard.getCollection();
                    return (collection != null) ? collection.getCollectionName() : null;
                }, Collectors.counting()));

        // Get all collection names from flashcardCollections
        Set<String> allCollectionNames = flashcardCollections.stream()
                .map(FlashcardCollection::getCollectionName)
                .collect(Collectors.toSet());

        // Create collection info for each collection (including empty ones)
        return allCollectionNames.stream()
                .map(collectionName -> {
                    Map<String, Object> collectionInfo = new HashMap<>();
                    collectionInfo.put("nameCollection", collectionName);
                    collectionInfo.put("flashcards", flashcardsCountByCollection.getOrDefault(collectionName, 0L));
                    return collectionInfo;
                })
                .collect(Collectors.toList());
    }

    public FlashcardInfoResponse deleteCollectionByName(String nameCollection) throws OtherException {
        try {
            // Pobierz zalogowanego użytkownika
            String loggedInUsername = tokenInstance.getUserName();

            // Pobierz kolekcję na podstawie nazwy i autora
            List<FlashcardCollection> collectionsToDelete = collectionRepository.findByCollectionNameAndAuthor(nameCollection, loggedInUsername);

            if (!collectionsToDelete.isEmpty()) {
                // Usuń kolekcje i związane z nimi fiszki
                for (FlashcardCollection collection : collectionsToDelete) {
                    List<Flashcard> flashcardsToDelete = flashcardRepository.findByCollection(collection);
                    flashcardRepository.deleteAll(flashcardsToDelete);
                    collectionRepository.delete(collection);
                }

                return FlashcardInfoResponse.builder().response("Collection deleted successfully.").build();
            } else {
                throw new OtherException("Collection not found or could not be deleted.");
            }
        } catch (Exception e) {
            throw new OtherException(e.getMessage());
        }
    }

}
