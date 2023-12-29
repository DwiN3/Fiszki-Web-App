import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './pages/register/register.component';
import { PlaceholderDirective } from './shared/ui/alert/directive/placeholder.directive';
import { UserModule } from './pages/user/user.module';
import { HomeGuard } from './pages/home/services/home-guard';
import { SharedModule } from './shared/shared.module';
import { StoreModule } from '@ngrx/store';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    PlaceholderDirective,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    SharedModule,
    UserModule,
    StoreModule.forRoot({}, {}),
    FontAwesomeModule,
  ],
  providers: [HomeGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
