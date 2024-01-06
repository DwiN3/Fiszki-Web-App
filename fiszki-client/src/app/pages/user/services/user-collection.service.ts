import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { FlashcardCollectionModel } from "../pages/user-profile-router/pages/user-collections/models/flashcard-collection.model";

@Injectable({providedIn : 'root'})
export class UserCollectionService
{
    url : string = 'http://localhost:8080/flashcards/';
    token : string | null = localStorage.getItem('token');
    headers : HttpHeaders = new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
    });

    constructor(private http : HttpClient){}

    GetCollections() : Observable<FlashcardCollectionModel[]>
    {
        return this.http.get<any>(this.url + 'collections', {headers : this.headers})
            .pipe(
                map((data : any[]) => data.map(collection => this.mapToCollection(collection)))
            );
    }

    AddCollection(collectionName : string)
    {
        const data = {collectionName : collectionName}
        return this.http.post<any>(this.url + 'add_collection', data, {headers : this.headers})
    }

    DeleteCollection(collectionName : string)
    {
        console.log(collectionName);
        return this.http.delete<any>(this.url + 'collection/' + collectionName, {headers : this.headers});
    } 

    private mapToCollection(data : any) : FlashcardCollectionModel
    {
        return new FlashcardCollectionModel(data.name_kit, data.flashcards.length)
    }

}