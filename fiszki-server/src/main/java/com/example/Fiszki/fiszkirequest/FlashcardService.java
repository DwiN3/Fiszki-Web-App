package com.example.Fiszki.fiszkirequest;

import com.example.Fiszki.Instance.TokenInstance;
import com.example.Fiszki.security.auth.AuthenticationResponse;
import com.example.Fiszki.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
}
