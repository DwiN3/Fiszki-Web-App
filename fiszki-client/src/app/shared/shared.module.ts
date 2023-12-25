import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './ui/alert/alert.component';
import { PlaceholderDirective } from './ui/alert/directive/placeholder.directive';

@NgModule({
  declarations: [
    AlertComponent,
    PlaceholderDirective,
    AlertComponent,
  ],
  imports: [CommonModule],
  exports: [
    PlaceholderDirective,
    AlertComponent,
  ],
})
export class SharedModule {}