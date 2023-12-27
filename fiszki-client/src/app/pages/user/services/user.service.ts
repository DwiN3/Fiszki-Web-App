import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseCurrentUserModel } from "../models/base-current-user.model";

@Injectable({providedIn : 'root'})
export class UserService
{
    url : string = "http://localhost:8080/flashcards/auth/";

    constructor(private http : HttpClient){}

    GetUserInfo()
    {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization' : `Bearer ${token}`
        })

        return this.http.get<any>(this.url + 'info', {headers : headers});
    }

}