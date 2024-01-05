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

@Component({
  selector: 'app-categories-page',
  templateUrl: './categories-page.component.html',
  styleUrls: ['./categories-page.component.scss']
})
export class CategoriesPageComponent {

  @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;

  alertData : AlertModel | null = null;
  alertSub : Subscription | null = null;

  categories : CategoryModel[] = [];
  flashcards : BaseFlashcardInterface[] = []
  limit : number = 10;
  isLoading : boolean = false;

  constructor(private categoriesDataService : CategoriesDataService, private flashcardService : FlashcardService, private router : Router, private componentFactoryResolver : ComponentFactoryResolver, private store : Store<{gameSettings : GameSettingsState}>)
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
        {
          this.alertData = new AlertModel('Brak fiszek', 'Brak fiszek w danej katgorii, spróbuj poźniej', '')
          this.ShowAlert();
        }
        else
        {
          this.store.dispatch(setCollection({collection : this.flashcards}))
          this.router.navigate(['user/learning/game'])
        }
          
          
      }, err => {
        this.isLoading = false;
        this.alertData = new AlertModel('Błąd serwera', err.message, "Spróbuj ponownie później!")
          this.ShowAlert();
      })

    
  }
  
  private ShowAlert(): void
  {
    const alertCmpFactory = this.componentFactoryResolver.resolveComponentFactory(AlertComponent);
    
    const hostViewContainerRef = this.alertHost?.viewContainerRef;
    hostViewContainerRef?.clear();

    const componentRef = hostViewContainerRef?.createComponent(alertCmpFactory);

    if(this.alertData)
    {
      componentRef.instance.title = this.alertData.title;
      componentRef.instance.instructions = this.alertData.instructions;
      componentRef.instance.details = this.alertData.details;
    }

    this.alertSub = componentRef.instance.close.subscribe(() => 
    {
      this.alertSub?.unsubscribe();
      hostViewContainerRef.clear();
    })
  }

}
