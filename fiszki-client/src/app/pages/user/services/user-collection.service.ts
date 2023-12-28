import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { FlashcardCollectionModel } from "src/app/shared/models/flashcards-collection.model";

@Injectable({providedIn : 'root'})
export class UserCollectionService
{
    url = 'http://localhost:8080/flashcards/collections';

    constructor(private http : HttpClient){}

    GetCollections()
    {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        });

        return this.http.get(this.url, {headers : headers});
    }

}