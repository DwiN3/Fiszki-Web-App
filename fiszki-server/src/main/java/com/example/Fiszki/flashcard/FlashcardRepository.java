package com.example.Fiszki.flashcard;

import com.example.Fiszki.flashcard.add.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    boolean existsByWord(String word);
    boolean existsByTranslatedWord(String translatedWord);
}
