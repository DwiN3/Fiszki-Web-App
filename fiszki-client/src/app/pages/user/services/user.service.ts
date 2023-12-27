import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UserLevelModel } from "../models/user-level.model";

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

    GetUserLevel()
    {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Authorization' : `Bearer ${token}`
        })

        return this.http.get<UserLevelModel>(this.url + 'level', {headers : headers});
    }

}