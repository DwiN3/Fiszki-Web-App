import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';
import { CarouselState } from './store/carousel.state';
import { faArrowLeft, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { decrementPage, incrementPage } from './store/carousel.actions';
import { CarouselSettingsService } from './services/carousel-settings.service';


@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss'],
})
export class UserCollectionsComponent implements OnInit{
  
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;

  carousel$! : Observable<CarouselState>;
  
  currentPage : number = 0;
  collectionQuantity : number = 0;
  pagesQuantity : number = 0;

  constructor(private userCollectionService : UserCollectionService, private store : Store<{carousel : CarouselState}>){}

  ngOnInit(): void {
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

}
