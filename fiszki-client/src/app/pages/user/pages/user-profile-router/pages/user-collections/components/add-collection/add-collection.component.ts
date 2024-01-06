import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faRemove } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';
import { FlashcardCollectionModel } from '../../models/flashcard-collection.model';
import { changeCollectionQuantity } from '../../store/carousel.actions';
import { CarouselState } from '../../store/carousel.state';
import { addCollection } from '../../store/collections.actions';
import { CollectionsState } from '../../store/collections.state';

@Component({
  selector: 'app-add-collection',
  templateUrl: './add-collection.component.html',
  styleUrls: ['./add-collection.component.scss']
})
export class AddCollectionComponent {

  faRemove = faRemove;

  @ViewChild('form') form : NgForm | null = null;
  @Output() closeForm = new EventEmitter<boolean>();

  collectionName : string = '';
  isLoading : boolean = false;

  constructor(private userCollectionService : UserCollectionService, private store : Store<{collections : CollectionsState}>, private carouselStore : Store<{carousel : CarouselState}>){}

  CloseForm() : void
  {
    this.closeForm.emit(false);
  }

  OnAdd(collectionName : string) : void
  {
    if(this.form && !this.form.valid)
      return

      this.isLoading = true;
    
    this.userCollectionService.AddCollection(collectionName)
      .subscribe(data => {
          const collection = new FlashcardCollectionModel(collectionName, 0);
          this.store.dispatch(addCollection({collection : collection}))
          this.carouselStore.dispatch(changeCollectionQuantity({value : 1}))
          this.isLoading = false;
      }, err => {
        console.log(err);
        this.isLoading = false;
      })
      
  }

}
