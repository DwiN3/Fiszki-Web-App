import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { BaseUserModel } from 'src/app/shared/models/base-user.model';
import { AccountService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  @ViewChild('form') loginForm : NgForm | null = null;
  error : string | null = null;
  email : string = '';
  password : string = '';

  constructor(private accountService : AccountService){}

  Submit() : void
  {
      if(this.loginForm?.valid === false)
        return

      const userData = new BaseUserModel(this.email, this.password);
      this.accountService.Login(userData)
        .subscribe(resData => {
            console.log(resData)
          }, error => {
            console.log(error);
          });
  }

}
