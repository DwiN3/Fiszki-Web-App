import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faRemove } from '@fortawesome/free-solid-svg-icons';
import { CategoriesDataService } from 'src/app/pages/user/pages/learning-page/pages/categories-page/services/categories-data.service';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';

@Component({
  selector: 'app-flashcard-edit-form',
  templateUrl: './flashcard-edit-form.component.html',
  styleUrls: ['./flashcard-edit-form.component.scss']
})
export class FlashcardEditFormComponent {

  @ViewChild('form') form : NgForm | null = null;
  faCross = faRemove;

  @Output() closeFormEvent = new EventEmitter<boolean>();

  options : string[] = [];
  selectedCategory: string = 'sport';
  flashcard : BaseFlashcardInterface; 
  
  constructor(private categoriesData : CategoriesDataService)
  {
    this.flashcard = 
    {
      id : null,
      word: '',
      translatedWord: '',
      example: '',
      translatedExample: '',
      category: ''
    };
    
    categoriesData.categories.forEach(element => {
        this.options.push(element.categoryName);
    });
  }

  Submit() : void 
  {
    if(this.form?.valid === false)
      return

    this.flashcard.category = this.selectedCategory;
    console.log(this.flashcard);
  }

  CloseForm() : void
  {
    this.closeFormEvent.emit(false);
  }

}
