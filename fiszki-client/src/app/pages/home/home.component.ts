import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { distinctUntilChanged, Observable, Subscription } from 'rxjs';
import { BaseUserModel } from 'src/app/shared/models/base-user.model';
import { AccountService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy{

  @ViewChild('form') loginForm : NgForm | null = null;
  error : string | null = null;
  subscription : Subscription | null = null;
  email : string = '';
  password : string = '';

  constructor(private accountService : AccountService){}

  ngAfterViewInit(): void 
  {
    if(this.loginForm)
    {
      this.subscription = this.loginForm.valueChanges ? this.loginForm.valueChanges.pipe(distinctUntilChanged())
        .subscribe(() => {
          this.error = null;
        })
        : null
    }
  }

  ngOnDestroy(): void 
  {
    if(this.subscription)
      this.subscription.unsubscribe();
  }

  Submit() : void
  {
      if(this.loginForm?.valid === false)
        return

      const userData = new BaseUserModel(this.email, this.password);
      this.accountService.Login(userData)
        .subscribe(resData => {
            localStorage.setItem('token', JSON.stringify(resData.response).replace(/"/g, ''));
          }, error => {
            this.error = error.error.response;
          });
  }

}
