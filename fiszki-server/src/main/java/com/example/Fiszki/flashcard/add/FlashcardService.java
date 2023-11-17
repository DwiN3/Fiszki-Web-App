package com.example.Fiszki.flashcard.add;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

    public FlashcardResponse addFlashcard(FlashcardRequest request) {
        Flashcard flashcard = Flashcard.builder()
                .collectionName(request.getCollectionName())
                .language(request.getLanguage())
                .category(request.getCategory())
                .word(request.getWord())
                .translatedWord(request.getTranslatedWord())
                .example(request.getExample())
                .translatedExample(request.getTranslatedExample())
                .build();

        flashcardRepository.save(flashcard);

        return FlashcardResponse.builder().response("Flashcard added successfully").build();
    }

    public FlashcardShowResponse showFlashcardById(Integer flashcardId) {
        // Tutaj możesz wykorzystać metody z FlashcardRepository do pobrania fiszki na podstawie ID
        Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);

        if (flashcardOptional.isPresent()) {
            Flashcard flashcard = flashcardOptional.get();
            return FlashcardShowResponse.builder()
                    .word(flashcard.getWord())
                    .translatedWord(flashcard.getTranslatedWord())
                    .example(flashcard.getExample())
                    .translatedExample(flashcard.getTranslatedExample())
                    .build();
        } else {
            // Obsługa przypadku, gdy nie znaleziono fiszki o danym ID
            return FlashcardShowResponse.builder().build();
        }
    }
}
