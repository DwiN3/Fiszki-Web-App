import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn : 'root'})
export class FlashcardService
{
    url : string = 'http://localhost:8080/flashcards/'

    constructor(private http : HttpClient){}

    // http://localhost:8080/flashcards/category/{category}
    // {
    //     "limit": ""
    // }

    GetFlashcardsByCategory(categoryName : string, limit : number)
    {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization' : `Bearer ${token}`
        })
        return this.http.get<any>(this.url + categoryName, { headers : headers})
    }
}