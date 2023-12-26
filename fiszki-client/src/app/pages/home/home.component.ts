import { Component, ComponentFactoryResolver, OnDestroy, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { distinctUntilChanged, Subscription } from 'rxjs';
import { AlertModel } from 'src/app/shared/models/alert.model';
import { BaseUserModel } from 'src/app/shared/models/base-user.model';
import { AccountService } from 'src/app/shared/services/user.service';
import { AlertComponent } from 'src/app/shared/ui/alert/alert.component';
import { PlaceholderDirective } from 'src/app/shared/ui/alert/directive/placeholder.directive';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy{

  @ViewChild('form') loginForm : NgForm | null = null;
  @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;
  error : string | null = null;
  subscription : Subscription | null = null;
  userData : BaseUserModel = new BaseUserModel('', '');
  isLoading : boolean = false;
  alertSub : Subscription | null = null;
  alertData : AlertModel = new AlertModel('', '', '');

  constructor(private accountService : AccountService, private router : Router, private componentFactoryResolver : ComponentFactoryResolver){}

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
            if(error.status === 401)
              this.error = "Zły email lub hasło!";
            else
            {
              this.alertData.title = "Bład servera!";
              this.alertData.details = error.message;
              this.alertData.instructions = "Spróbuj ponownie później!";
              this.ShowAlert();
            }  
            this.isLoading = false;
          });
  }

  private ShowAlert(): void
  {
    const alertCmpFactory = this.componentFactoryResolver.resolveComponentFactory(AlertComponent);
    
    const hostViewContainerRef = this.alertHost?.viewContainerRef;
    hostViewContainerRef?.clear();

    const componentRef = hostViewContainerRef?.createComponent(alertCmpFactory);

    componentRef.instance.title = this.alertData.title;
    componentRef.instance.instructions = this.alertData.instructions;
    componentRef.instance.details = this.alertData.details;

    this.alertSub = componentRef.instance.close.subscribe(() => 
    {
      this.alertSub?.unsubscribe();
      hostViewContainerRef.clear();
    })
  }

}
