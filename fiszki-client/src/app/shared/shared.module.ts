import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './ui/alert/alert.component';
import { LoaderComponent } from './ui/loader/loader.component';

@NgModule({
  declarations: [
    AlertComponent,
    LoaderComponent,
  ],
  imports: [
    CommonModule,
  ],
  exports: [
    AlertComponent,
    LoaderComponent,
  ],
})
export class SharedModule {}