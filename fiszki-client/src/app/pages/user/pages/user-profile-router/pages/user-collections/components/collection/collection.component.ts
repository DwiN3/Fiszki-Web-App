import { Component, Input } from '@angular/core';
import { faPlusCircle, faTrash, faEdit } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
  styleUrls: ['./collection.component.scss']
})
export class CollectionComponent {
    @Input() collectionName : string = '';
    @Input() flashcardsQuantity : number = 0;

    faPlusCircle = faPlusCircle;
    faTrash = faTrash;
    faEdit = faEdit;

}
