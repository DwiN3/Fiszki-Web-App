import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './ui/alert/alert.component';
import { LoaderComponent } from './ui/loader/loader.component';
import { LogoComponent } from './ui/logo/logo.component';

@NgModule({
  declarations: [
    AlertComponent,
    LoaderComponent,
    LogoComponent,
  ],
  imports: [
    CommonModule,
  ],
  exports: [
    AlertComponent,
    LoaderComponent,
    LogoComponent,
  ],
})
export class SharedModule {}