import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';
import { CarouselState } from './store/carousel.state';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { decrementPage, incrementPage, setCollectionQuantity } from './store/carousel.actions';
import { FlashcardCollectionModel } from './models/flashcard-collection.model';
import { CollectionsState } from './store/collections.state';
import { setCollection } from './store/collections.actions';

@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss'],
})
export class UserCollectionsComponent implements OnInit{
  
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;

  carousel$! : Observable<CarouselState>;
  collections$! : Observable<CollectionsState>;
  
  currentPage : number = 0;
  collectionQuantity : number = 0;
  pagesQuantity : number = 0;
  
  flashcardsCollection : FlashcardCollectionModel[] = []
  
  isLoading : boolean = true;
  formState : boolean = false;
  

  constructor(private userCollectionService : UserCollectionService, private store : Store<{carousel : CarouselState}>, private collectionStore : Store<{collections : CollectionsState}>){}

  ngOnInit(): void {

    this.userCollectionService.GetCollections()
      .subscribe(data => {
          this.collectionStore.dispatch(setCollection({collections : data}));
          this.isLoading = false;
      }, err => {
        console.log(err)
      })

    this.store.dispatch(setCollectionQuantity({quantity : this.flashcardsCollection.length}))

    this.collections$ = this.collectionStore.select('collections');
    this.collections$
      .subscribe(data => {
        this.flashcardsCollection = data.collections
      })

    this.carousel$ = this.store.select('carousel');
    this.carousel$
      .subscribe(data => {
        this.currentPage = data.currentPage;
        this.collectionQuantity = data.collectionQuantity
        this.pagesQuantity = data.pageQuantity;
      })
  }

 
  IncrementPage()
  {
    if(this.currentPage >= this.pagesQuantity)
      return
    this.store.dispatch(incrementPage())
  }

  DecrementPage()
  {
    if(this.currentPage <= 0)
      return
    this.store.dispatch(decrementPage())
  }
  
  ChangeFormState(state : boolean) : void
  {
    this.formState = state;
  }

}
