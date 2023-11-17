package com.example.Fiszki.flashcard;

import com.example.Fiszki.flashcard.add.Flashcard;
import com.example.Fiszki.flashcard.show.FlashcardShowResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    boolean existsByWord(String word);
    boolean existsByTranslatedWord(String translatedWord);
    List<Flashcard> findByCategory(String category);
}
