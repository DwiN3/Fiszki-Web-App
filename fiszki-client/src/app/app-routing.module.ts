import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { HomeGuard } from './pages/home/services/home-guard';
import { RegisterComponent } from './pages/register/register.component';

const routes : Routes = 
[
  { path: '', component : HomeComponent, canActivate : [HomeGuard] },
  { path : 'register', component : RegisterComponent, canActivate : [HomeGuard] },
  { path : 'user', redirectTo: '/user', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
