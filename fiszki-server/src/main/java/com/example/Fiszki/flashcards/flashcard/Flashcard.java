package com.example.Fiszki.flashcards.flashcard;

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
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto generating id
    private Integer id;
    private String author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_id")
    private FlashcardCollection collection;

    private String category;
    private String word;
    private String translatedWord;
    private String example;
    private String translatedExample;

//    public String getCollectionName() {
//        return collectionName;
//    }


    public FlashcardCollection getCollection() {
        return collection;
    }

    public void setCollection(FlashcardCollection collection) {
        this.collection = collection;
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
