import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';
import { BaseFlashcardInterface } from 'src/app/shared/models/flashcard.interface';

@Component({
  selector: 'app-user-collection-edit',
  templateUrl: './user-collection-edit.component.html',
  styleUrls: ['./user-collection-edit.component.scss']
})
export class UserCollectionEditComponent {

  collection : BaseFlashcardInterface[] = [];
  isFormOpen : boolean = false;
  collectionName : string | null = null;
  isLoading : boolean = true;

  constructor(private activeRoute : ActivatedRoute, private collectionService : UserCollectionService)
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

  

}
