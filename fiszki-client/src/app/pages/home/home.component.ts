import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

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

  Login() : void
  {
    console.log(this.loginForm);
  }

}
