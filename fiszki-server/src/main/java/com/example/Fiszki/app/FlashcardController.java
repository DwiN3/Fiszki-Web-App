package com.example.Fiszki.app;

import com.example.Fiszki.models.Flashcards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/add-flashcard")
public class FlashcardController {

    Flashcards fiszka = new Flashcards("1","english", "category","kot",  "cat", "kot lubi mleko", "cat like milk");

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Twoja fiszka to: "+fiszka.getWord() + " tłumaczenie "+ fiszka.getTranslatedWord());
    }
}
