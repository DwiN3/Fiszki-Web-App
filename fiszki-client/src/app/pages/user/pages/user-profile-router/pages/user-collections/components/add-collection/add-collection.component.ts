import { Component, EventEmitter, Output } from '@angular/core';
import { faRemove } from '@fortawesome/free-solid-svg-icons';
import { UserCollectionService } from 'src/app/pages/user/services/user-collection.service';

@Component({
  selector: 'app-add-collection',
  templateUrl: './add-collection.component.html',
  styleUrls: ['./add-collection.component.scss']
})
export class AddCollectionComponent {

  faRemove = faRemove;

  @Output() closeForm = new EventEmitter<boolean>();

  isLoading : boolean = false;

  constructor(private userCollectionService : UserCollectionService){}

  CloseForm() : void
  {
    this.closeForm.emit(false);
  }

  OnAdd(collectionName : string) : void
  {
    this.userCollectionService.AddCollection(collectionName)
      .subscribe(data => {
        console.log(data);
      }, err => {
        console.log(err);
      })
  }

}
