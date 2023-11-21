package com.example.Fiszki.app;

import com.example.Fiszki.Instance.TokenInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app-controller")
public class AppController {
    TokenInstance tokenInstance = TokenInstance.getInstance();

    @GetMapping
    public ResponseEntity<String> sayHello() {return ResponseEntity.ok("Hello");}
}
