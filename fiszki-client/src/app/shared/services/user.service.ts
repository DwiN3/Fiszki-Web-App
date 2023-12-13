import { Injectable } from "@angular/core";
import { BaseUserModel } from "../models/base-user.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable({providedIn : 'root'})
export class AccountService
{
    url="http://localhost:8080/flashcards/auth/";
    constructor(private http : HttpClient){}

    Login(userData : BaseUserModel)
    {
        return this.http.post<BaseUserModel>(this.url + 'login', userData);
    }
}