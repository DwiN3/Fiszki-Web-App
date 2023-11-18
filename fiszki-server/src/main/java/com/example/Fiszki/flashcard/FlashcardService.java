package com.example.Fiszki.flashcard;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.flashcard.add.FlashcardAdd;
import com.example.Fiszki.flashcard.add.FlashcardAddRequest;
import com.example.Fiszki.flashcard.add.FlashcardAddResponse;
import com.example.Fiszki.flashcard.collection.FlashcardCollectionResponse;
import com.example.Fiszki.flashcard.edit.FlashcardEditRequest;
import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private TokenInstance tokenInstance = TokenInstance.getInstance();

    public FlashcardService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public FlashcardAddResponse addFlashcard(FlashcardAddRequest request) {

        // Sprawdź, czy wszystkie wymagane pola są ustawione
        if (isNullOrEmpty(request.getCollectionName()) || isNullOrEmpty(request.getLanguage()) ||
                isNullOrEmpty(request.getCategory()) || isNullOrEmpty(request.getWord()) ||
                isNullOrEmpty(request.getTranslatedWord()) || isNullOrEmpty(request.getExample()) ||
                isNullOrEmpty(request.getTranslatedExample())) {
            return FlashcardAddResponse.builder().response("All fields must be filled").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym słowie
        if (flashcardRepository.existsByWord(request.getWord())) {
            return FlashcardAddResponse.builder().response("Flashcard with the given word already exists").build();
        }

        // Sprawdź, czy istnieje fiszka o podanym przetłumaczonym słowie
        if (flashcardRepository.existsByTranslatedWord(request.getTranslatedWord())) {
            return FlashcardAddResponse.builder().response("Flashcard with the given translated word already exists").build();
        }

        // Sprawdź, czy w polach collectionName, language, category, word i translatedWord występuje tylko jedno słowo
        if (!isSingleWord(request.getCollectionName()) || !isSingleWord(request.getLanguage()) ||
                !isSingleWord(request.getCategory())) {
            return FlashcardAddResponse.builder().response("Fields not a single word").build();
        }

        FlashcardAdd flashcardAdd = FlashcardAdd.builder()
                .collectionName(request.getCollectionName())
                .language(request.getLanguage())
                .category(request.getCategory())
                .word(request.getWord())
                .translatedWord(request.getTranslatedWord())
                .example(request.getExample())
                .translatedExample(request.getTranslatedExample())
                .author(tokenInstance.getUserName())
                .build();

        flashcardRepository.save(flashcardAdd);

        return FlashcardAddResponse.builder().response("Flashcard added successfully").build();
    }

    public FlashcardAddResponse editFlashcard(Integer flashcardId, FlashcardEditRequest request) {
        // Sprawdź, czy wszystkie wymagane pola są ustawione
        if (!isSingleWord(request.getCollectionName())) {
            return FlashcardAddResponse.builder().response("All fields must be filled").build();
        }

        // Sprawdź, czy w polach word i translatedWord występuje tylko jedno słowo
        if (!isSingleWord(request.getWord()) || !isSingleWord(request.getTranslatedWord())) {
            return FlashcardAddResponse.builder().response("Fields word and translatedWord must contain a single word").build();
        }

        Optional<FlashcardAdd> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            FlashcardAdd flashcardAdd = flashcardOptional.get();

            flashcardAdd.setCollectionName(request.getCollectionName());
            flashcardAdd.setWord(request.getWord());
            flashcardAdd.setTranslatedWord(request.getTranslatedWord());
            flashcardAdd.setExample(request.getExample());
            flashcardAdd.setTranslatedExample(request.getTranslatedExample());

            flashcardRepository.save(flashcardAdd);

            return FlashcardAddResponse.builder().response("Flashcard updated successfully").build();
        } else {
            return FlashcardAddResponse.builder().response("Flashcard not found").build();
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


    public FlashcardShowResponse showFlashcardById(Integer flashcardId) {
        // Tutaj możesz wykorzystać metody z FlashcardRepository do pobrania fiszki na podstawie ID
        Optional<FlashcardAdd> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            FlashcardAdd flashcardAdd = flashcardOptional.get();
            return FlashcardShowResponse.builder()
                    .id(flashcardAdd.getId())
                    .word(flashcardAdd.getWord())
                    .translatedWord(flashcardAdd.getTranslatedWord())
                    .example(flashcardAdd.getExample())
                    .translatedExample(flashcardAdd.getTranslatedExample())
                    .author(flashcardAdd.getAuthor())
                    .build();
        } else {
            return FlashcardShowResponse.builder().build();
        }
    }

    public void deleteFlashcardById(Integer flashcardId) {
        flashcardRepository.deleteById(flashcardId);
    }

    public List<FlashcardShowResponse> showFlashcardsByCategory(String category) {
        List<FlashcardAdd> flashcardAdds = flashcardRepository.findByCategory(category);

        return flashcardAdds.stream()
                .map(flashcardAdd -> FlashcardShowResponse.builder()
                        .id(flashcardAdd.getId())
                        .word(flashcardAdd.getWord())
                        .translatedWord(flashcardAdd.getTranslatedWord())
                        .example(flashcardAdd.getExample())
                        .translatedExample(flashcardAdd.getTranslatedExample())
                        .author(flashcardAdd.getAuthor())
                        .build())
                .collect(Collectors.toList());
    }

    public List<FlashcardCollectionResponse> showAllCollection(String author) {
        List<FlashcardAdd> flashcardAdds = flashcardRepository.findByAuthor(author);

        // Group flashcards by collection name
        return flashcardAdds.stream()
                .collect(Collectors.groupingBy(FlashcardAdd::getCollectionName))
                .entrySet().stream()
                .map(entry -> {
                    String collectionName = entry.getKey();
                    List<FlashcardShowResponse> flashcards = entry.getValue().stream()
                            .map(flashcardAdd -> FlashcardShowResponse.builder()
                                    .id(flashcardAdd.getId())
                                    .word(flashcardAdd.getWord())
                                    .translatedWord(flashcardAdd.getTranslatedWord())
                                    .example(flashcardAdd.getExample())
                                    .translatedExample(flashcardAdd.getTranslatedExample())
                                    .author(flashcardAdd.getAuthor())
                                    .build())
                            .collect(Collectors.toList());

                    return FlashcardCollectionResponse.builder()
                            .name_kit(collectionName)
                            .flashcards(flashcards)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<FlashcardShowResponse> showCollectionByName(String nameCollection, String author) {
        List<FlashcardShowResponse> flashcards = flashcardRepository.findByCollectionNameAndAuthor(nameCollection, author)
                .stream()
                .map(flashcardAdd -> FlashcardShowResponse.builder()
                        .id(flashcardAdd.getId())
                        .word(flashcardAdd.getWord())
                        .translatedWord(flashcardAdd.getTranslatedWord())
                        .example(flashcardAdd.getExample())
                        .translatedExample(flashcardAdd.getTranslatedExample())
                        .author(flashcardAdd.getAuthor())
                        .build())
                .collect(Collectors.toList());

        return flashcards;
    }


    public List<Map<String, Object>> showCollectionInfo(String author) {
        List<FlashcardAdd> flashcardAdds = flashcardRepository.findByAuthor(author);

        return flashcardAdds.stream()
                .collect(Collectors.groupingBy(FlashcardAdd::getCollectionName))
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

    @Transactional
    public void deleteCollectionByName(String nameCollection, String author) {
        flashcardRepository.deleteByCollectionNameAndAuthor(nameCollection, author);
    }
}
