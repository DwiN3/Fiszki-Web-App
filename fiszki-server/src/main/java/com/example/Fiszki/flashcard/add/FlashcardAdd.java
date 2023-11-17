package com.example.Fiszki.flashcard.add;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flashcards")
public class FlashcardAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto generating id
    private Integer id;
    private String author;
    private String collectionName;
    private String language;
    private String category;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;

    public String getCollectionName() {
        return collectionName;
    }

    public String getLanguage() {
        return language;
    }

    public String getCategory() {
        return category;
    }

    public String getWord() {
        return word;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public String getExample() {
        return example;
    }

    public String getTranslatedExample() {
        return translatedExample;
    }
    public String getAuthor() {
        return author;
    }
}