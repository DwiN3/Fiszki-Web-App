package com.example.Fiszki.flashcard;

import com.example.Fiszki.flashcard.add.FlashcardAdd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<FlashcardAdd, Integer> {
    boolean existsByWord(String word);
    boolean existsByTranslatedWord(String translatedWord);
    List<FlashcardAdd> findByCategory(String category);
    List<FlashcardAdd> findByAuthor(String author);
    List<FlashcardAdd> findByCollectionNameAndAuthor(String collectionName, String author);
    void deleteByCollectionNameAndAuthor(String collectionName, String author);
}
