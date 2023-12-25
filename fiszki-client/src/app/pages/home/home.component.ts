import { Component, OnDestroy, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { distinctUntilChanged, Subscription } from 'rxjs';
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
  userData : BaseUserModel = new BaseUserModel('', '');
  isLoading : boolean = false;

  constructor(private accountService : AccountService, private router : Router){}

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

      this.isLoading = true;

      this.accountService.Login(this.userData)
        .subscribe(resData => {
            localStorage.setItem('token', JSON.stringify(resData.response).replace(/"/g, ''));
            this.router.navigate(['user']);
          }, error => {
            this.error = error.error.response;
            this.isLoading = false;
          });

  }

}
