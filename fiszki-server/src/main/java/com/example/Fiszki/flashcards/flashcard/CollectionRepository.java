package com.example.Fiszki.flashcards.flashcard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<FlashcardCollection, Integer> {
    Optional<FlashcardCollection> findByCollectionName(String collectionName);
}
