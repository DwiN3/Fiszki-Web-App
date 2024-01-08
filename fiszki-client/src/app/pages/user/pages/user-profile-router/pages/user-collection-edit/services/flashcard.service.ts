import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { FlashcardAddInterface } from "src/app/shared/models/flashcard-add.interface";

@Injectable({providedIn : 'root'})
export class FlashcardService
{
    url : string = 'http://localhost:8080/flashcards/';
    token : string | null = localStorage.getItem('token');
    headers : HttpHeaders = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
    });

    constructor(private http : HttpClient){}

    AddFlashcard(flashcard : FlashcardAddInterface)
    {
        console.log(flashcard);
        return this.http.post<any>(this.url + 'add-flashcard', flashcard, { headers : this.headers })
    }
}