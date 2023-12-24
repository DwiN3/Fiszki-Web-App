import { Component, OnDestroy, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { distinctUntilChanged, Subscription } from 'rxjs';
import { RegisterUserModel } from 'src/app/shared/models/register-user.model';
import { AccountService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy{

  @ViewChild('form') registerForm : NgForm | null = null;
  error : string | null = null;
  userData : RegisterUserModel = new RegisterUserModel('', '', '', '', '')
  subscription : Subscription | null = null;

  constructor(private accountService : AccountService){}

  ngAfterViewInit(): void 
  {
    if(this.registerForm)
    {
      this.subscription = this.registerForm.valueChanges ? this.registerForm.valueChanges
      .pipe(distinctUntilChanged())
        .subscribe(() => {
          this.error = null;
        })
        : null
    }
  }

  ngOnDestroy(): void {
    if(this.subscription)
      this.subscription.unsubscribe();
  }

  SubmitForm() : void
  {
    if(this.registerForm?.valid === false)
      return
    
    this.accountService.Register(this.userData)
      .subscribe(resData => {
        console.log(resData)
      }, err => {
        console.log(err)
        this.error = err.error.response;
      })
  }

}
