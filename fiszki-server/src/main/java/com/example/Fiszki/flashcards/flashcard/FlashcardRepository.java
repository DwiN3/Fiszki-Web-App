package com.example.Fiszki.flashcards.flashcard;

import com.example.Fiszki.flashcards.flashcard.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    boolean existsByWord(String word);
    boolean existsByTranslatedWord(String translatedWord);
    List<Flashcard> findByCategory(String category);
    List<Flashcard> findByAuthor(String author);
    List<Flashcard> findByCollectionNameAndAuthor(String collectionName, String author);
    void deleteByCollectionNameAndAuthor(String collectionName, String author);
}
