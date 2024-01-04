import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { setCategory, setCollection, setLimit } from '../../store/game-settings.action';
import { GameSettingsState } from '../../store/game.state';
import { CategoryModel } from './models/category.model';
import { CategoriesDataService } from './services/categories-data.service';

@Component({
  selector: 'app-categories-page',
  templateUrl: './categories-page.component.html',
  styleUrls: ['./categories-page.component.scss']
})
export class CategoriesPageComponent {

  categories : CategoryModel[] = [];
  limit : number = 10;

  constructor(private categoriesDataService : CategoriesDataService, private store : Store<{gameSettings : GameSettingsState}>, private router : Router)
  {
    this.categories = categoriesDataService.categories;
  }

  SetCategory(index : number) : void
  {
    const category = this.categories[index].categoryName
    this.store.dispatch(setCollection({ collectionName : ''}))
    this.store.dispatch(setLimit({limit : this.limit}))
    this.store.dispatch(setCategory({category : category}));
    this.router.navigate(['user/learning/game'])
  }
  

}
