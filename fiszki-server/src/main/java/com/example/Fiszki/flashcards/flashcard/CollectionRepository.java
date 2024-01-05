package com.example.Fiszki.flashcards.flashcard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<FlashcardCollection, Integer> {
    Optional<FlashcardCollection> findByCollectionName(String collectionName);
    boolean existsByCollectionNameAndAuthor(String collectionName, String author);
    List<FlashcardCollection> findByCollectionNameAndAuthor(String collectionName, String author);
}
