import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { FlashcardCollectionModel } from "src/app/shared/models/flashcards-collection.model";

@Injectable({providedIn : 'root'})
export class UserCollectionService
{
    url = 'http://localhost:8080/flashcards/collections-info';
    token : string | null = localStorage.getItem('token');
    headers : HttpHeaders = new HttpHeaders(
            {
                'Authorization' : `Bearer ${this.token}`,
                'Content-Type': 'application/json'
            }
        )


    constructor(private http : HttpClient){}

    GetCollections()
    {
        return this.http.get<FlashcardCollectionModel[]>(this.url, {headers : this.headers});
    }
}