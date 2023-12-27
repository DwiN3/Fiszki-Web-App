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
import { LoaderComponent } from './shared/ui/loader/loader.component';
import { HomeGuard } from './pages/home/services/home-guard';
import { LogoComponent } from './shared/ui/logo/logo.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    PlaceholderDirective,
    LoaderComponent,
    LogoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    UserModule,
  ],
  providers: [HomeGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
