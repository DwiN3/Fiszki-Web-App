package com.example.Fiszki.flashcards.flashcard;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String path;  // Możesz dostosować to pole do swoich potrzeb

    public ApiError(HttpStatus status, String message, String path) {
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}

