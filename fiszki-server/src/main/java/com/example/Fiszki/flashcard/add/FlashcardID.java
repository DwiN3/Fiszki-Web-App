package com.example.Fiszki.flashcard.add;


public class FlashcardID {
    private String word, translatedWord, example, translatedExample, _id;

    public FlashcardID() {

    }

    public FlashcardID(String word, String translatedWord, String example, String translatedExample) {
        this.word = word;
        this.translatedWord = translatedWord;
        this.example = example;
        this.translatedExample = translatedExample;
    }

    public FlashcardID(String _id, String word, String translatedWord, String example, String translatedExample) {
        this.word = word;
        this.translatedWord = translatedWord;
        this.example = example;
        this.translatedExample = translatedExample;
        this._id = _id;
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

    public String get_id() {
        return _id;
    }
}
