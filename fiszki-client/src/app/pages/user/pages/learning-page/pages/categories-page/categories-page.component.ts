import { Component, ComponentFactoryResolver, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FlashcardService } from 'src/app/pages/user/services/user.flashcards.service';
import { CategoryModel } from './models/category.model';
import { CategoriesDataService } from './services/categories-data.service';
import { PlaceholderDirective } from 'src/app/shared/ui/alert/directive/placeholder.directive';
import { AlertModel } from 'src/app/shared/models/alert.model';
import { AlertComponent } from 'src/app/shared/ui/alert/alert.component';
import { Subscription } from 'rxjs';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';
import { Store } from '@ngrx/store';
import { GameSettingsState } from '../../store/game.state';
import { setCollection } from '../../store/game-settings.action';
import { AlertService } from 'src/app/shared/ui/alert/service/alert.service';

@Component({
  selector: 'app-categories-page',
  templateUrl: './categories-page.component.html',
  styleUrls: ['./categories-page.component.scss']
})
export class CategoriesPageComponent {

  @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;

  categories : CategoryModel[] = [];
  flashcards : BaseFlashcardInterface[] = []
  limit : number = 10;
  isLoading : boolean = false;

  constructor(private categoriesDataService : CategoriesDataService, private flashcardService : FlashcardService, private router : Router, private store : Store<{gameSettings : GameSettingsState}>, private alertService : AlertService)
  {
    this.categories = categoriesDataService.categories;
  }

  SetCategory(index : number) : void
  {
    const category = this.categories[index].categoryName
    this.isLoading = true;

    this.flashcardService.GetFlashcardsByCategory(category, this.limit)
      .subscribe(data => {
        this.flashcards = data;
        this.isLoading = false;
        if(this.flashcards.length === 0)
          this.alertService.ShowAlert('Brak fiszek!', 'Brak fiszek w danej katgorii, spróbuj poźniej', '', this.alertHost);
        else
        {
          this.store.dispatch(setCollection({collection : this.flashcards}))
          this.router.navigate(['user/learning/game'])
        }
          
          
      }, err => {
        this.isLoading = false;
        this.alertService.ShowAlert('Błąd serwera!', err.message, 'Spróbuj ponownie później', this.alertHost);
      })

    
  }

}
