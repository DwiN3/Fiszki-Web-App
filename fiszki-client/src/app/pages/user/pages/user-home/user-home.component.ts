import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/shared/services/user.service';
import { BaseCurrentUserModel } from '../../models/base-current-user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit{

  currentUser : BaseCurrentUserModel | null = null;
  isLoading : boolean = true;

  constructor(private accountService : AccountService, private router : Router, private userService : UserService){}

  ngOnInit(): void {
    this.userService.GetUserInfo()
      .subscribe(res => {
        this.currentUser = new BaseCurrentUserModel(res.firstName, res.lastName);
      }, err => {
        console.log(err)
      })
      this.isLoading =  false;
  }

  LogOut() : void
  {
    this.accountService.LogOut();
    this.router.navigate(['']);
  }

}
