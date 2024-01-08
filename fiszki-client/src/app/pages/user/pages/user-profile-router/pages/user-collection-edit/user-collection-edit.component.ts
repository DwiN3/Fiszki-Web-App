import { Component, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';
import { PlaceholderDirective } from 'src/app/shared/ui/alert/directive/placeholder.directive';
import { AlertService } from 'src/app/shared/ui/alert/service/alert.service';
import { FlashcardService } from './services/flashcard.service';

@Component({
  selector: 'app-user-collection-edit',
  templateUrl: './user-collection-edit.component.html',
  styleUrls: ['./user-collection-edit.component.scss']
})
export class UserCollectionEditComponent {

  @ViewChild(PlaceholderDirective, { static: true }) alertHost!: PlaceholderDirective;

  collection : BaseFlashcardInterface[] = [];
  isFormOpen : boolean = false;
  collectionName : string | null = null;
  isLoading : boolean = true;

  constructor(private activeRoute : ActivatedRoute, private collectionService : UserCollectionService, private flashcardService : FlashcardService, private alertService : AlertService)
  {
    const queryParams = this.activeRoute.snapshot.queryParamMap;
    this.collectionName = queryParams.get('collectionName');

    if(this.collectionName === null)
      this.collectionName = '';
    
    this.collectionService.GetFlashCardsByCollection(this.collectionName)
      .subscribe(data => {
        this.collection = data;
        this.isLoading = false;
      }, err => {
        console.log(err);
      })
  }

  OpenForm() : void
  {
    this.isFormOpen = true;
  }

  OnDelete(id : number)
  {
    this.flashcardService.DeleteFlashcard(id)
      .subscribe(data => {
        this.collection = this.collection.filter(flashcard => flashcard.id !== id)
      }, err => {
        this.alertService.ShowAlert('Błąd serwera!', err.message, 'Spróbuj ponownie później!', this.alertHost);
      })
  }

  OnAdd(flashcard : BaseFlashcardInterface)
  {
    this.collection.push(flashcard);
  }

}
