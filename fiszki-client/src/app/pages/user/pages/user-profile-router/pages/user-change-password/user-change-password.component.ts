import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserPasswordExtendInterface } from './models/user-password-extend';

@Component({
  selector: 'app-user-change-password',
  templateUrl: './user-change-password.component.html',
  styleUrls: ['./user-change-password.component.scss']
})
export class UserChangePasswordComponent {

  @ViewChild('form') form : NgForm | null = null;

  userData : UserPasswordExtendInterface = 
  {
    email : '', 
    password : '', 
    newPassword : '', 
    reNewPassword : ''
  }

  Submit() : void
  {
    if(this.form?.valid === false)
      return

    
  }

}
