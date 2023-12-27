import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent {

  constructor(private accountService : AccountService, private router : Router){}

  LogOut() : void
  {
    this.accountService.LogOut();
    this.router.navigate(['']);
  }

}
