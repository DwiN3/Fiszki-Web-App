import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faRemove } from '@fortawesome/free-solid-svg-icons';
import { CategoriesDataService } from 'src/app/pages/user/pages/learning-page/pages/categories-page/services/categories-data.service';
import { FlashcardAddInterface } from 'src/app/shared/models/flashcard-add.interface';
import { FlashcardService } from '../../services/flashcard.service';
import { PlaceholderDirective } from 'src/app/shared/ui/alert/directive/placeholder.directive';
import { AlertService } from 'src/app/shared/ui/alert/service/alert.service';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';

@Component({
  selector: 'app-flashcard-edit-form',
  templateUrl: './flashcard-edit-form.component.html',
  styleUrls: ['./flashcard-edit-form.component.scss']
})
export class FlashcardEditFormComponent implements OnInit{

  @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;
  @ViewChild('form') form : NgForm | null = null;
  faCross = faRemove;

  @Output() closeFormEvent = new EventEmitter<boolean>();
  @Output() flashcardEvent = new EventEmitter<BaseFlashcardInterface>();
  @Input() collectionName : string | null = '';
  

  isLoading : boolean = false;
  options : string[] = [];
  selectedCategory: string = 'sport';
  flashcard : FlashcardAddInterface; 
  
  constructor(private categoriesData : CategoriesDataService, private flashcardService : FlashcardService, private alertService : AlertService)
  {
    this.flashcard = 
    {
      id : null,
      word: '',
      translatedWord: '',
      example: '',
      translatedExample: '',
      category: '',
      collectionName: '',
    };

    categoriesData.categories.forEach(element => {
        this.options.push(element.categoryName);
    });

  }

  ngOnInit(): void 
  {
    if(this.collectionName)
    this.flashcard.collectionName = this.collectionName;
  }

  Submit() : void 
  {
    if(this.form?.valid === false)
      return

    this.isLoading = true;
    this.flashcard.category = this.selectedCategory;

    this.flashcardService.AddFlashcard(this.flashcard)
      .subscribe(data => {
        this.isLoading = false;
        const addedFlashcard = data;
        this.flashcardEvent.emit(addedFlashcard);
      }, err => {
        this.isLoading = false;
        if(err.status === 400)
          this.alertService.ShowAlert('Błąd!', 'Podana fiszka już jest w zestawie!', '', this.alertHost);
          else
          this.alertService.ShowAlert('Błąd serwera!', err.message, 'Spróbuj ponownie później!', this.alertHost);
      })
  }

  CloseForm() : void
  {
    this.closeFormEvent.emit(false);
  }

}
